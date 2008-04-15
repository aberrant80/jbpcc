/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpcc.util.jpa;

import java.util.*;
import java.io.*;
import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.*;
import com.sun.mirror.util.*;

import org.apache.commons.lang.StringUtils;

public class BooleanMagicProcessor implements AnnotationProcessor {

    private final AnnotationProcessorEnvironment env;
    private AnnotationTypeDeclaration jpaBooleanDeclaration;
    private final static String NEW_LINE = System.getProperty("line.separator");
    private final static String CODE_HEADING =
            "\t//--- Lines below are generated by JBPCC BooleanMagicConvertor PROCESSOR" + NEW_LINE +
            "\t//--- START :" + NEW_LINE + NEW_LINE;
    private final static String CODE_REMARK_START =
            "\t//--- Line below (Field getter and setter) will remarked by JBPCC BooleanMagicConvertor PROCESSOR" + NEW_LINE +
            "\t//--- JBPCC will regenerate those getter and setter at the end of the src code " + NEW_LINE + 
            "\t/**" + NEW_LINE;
    
    private final static String CODE_REMARK_END =
            "\t//--- REMARK BY JBPCC BooleanMagicConvertor PROCESSOR" + NEW_LINE + 
            "\t*/" + NEW_LINE;
    
    private final static String CODE_TAIL =
            "\t//--- END" + NEW_LINE +
            "\t//--- GENERATED BY JBPCC BooleanMagicConvertor PROCESSOR" + NEW_LINE;

    BooleanMagicProcessor(AnnotationProcessorEnvironment env) {
        this.env = env;
        jpaBooleanDeclaration = (AnnotationTypeDeclaration) env.getTypeDeclaration(BooleanMagicProcessorFactory.JPA_BOOLEAN_MAP_ANNOTATION);
    }

    public void process() {
        DeclarationVisitor visitor = DeclarationVisitors.getDeclarationScanner(
                new ClassVisitor(), DeclarationVisitors.NO_OP);
        for (TypeDeclaration type : env.getSpecifiedTypeDeclarations()) {
            type.accept(visitor);
        }

    }

    private class ClassVisitor extends SimpleDeclarationVisitor {

        String srcFileName = null;

        /**
         * This method is getting annoying long, thanks to the very 
         * annoying com.sun.mirror API. I needs to refactor it a bit
         **/
        @Override
        public void visitClassDeclaration(ClassDeclaration c) {
            boolean booleanMagicAnnotationFound= false;
            String sourceFileText = null;
            StringBuffer generatedSourceBuf = null;
            PrintWriter writer = null;

            // Store all Class Method's line number into Array, we will use it to remark
            // Field's annotated with @BooleanMagic, setter and getter method.
            //
            ArrayList<Integer> methodLineArray = new ArrayList<Integer>();
            for (MemberDeclaration method : c.getMethods()) {
                methodLineArray.add(method.getPosition().line());
            }

            System.out.println("Processing Java Class:" + c.getQualifiedName());
            try {
                Collection<AnnotationMirror> annotations = c.getAnnotationMirrors();

                // We only needs to process Java Class annotated with JPA Entity
                TypeDeclaration entityAnnotation = env.getTypeDeclaration(BooleanMagicProcessorFactory.JAVAX_PERSISTANCE_ENTITY_ANNOTATION);
                for (AnnotationMirror mirror : annotations) {
                    if (mirror.getAnnotationType().getDeclaration().equals(entityAnnotation)) {
                        ArrayList<FieldDeclaration> annotatedFieldList = new ArrayList<FieldDeclaration>();
                        generatedSourceBuf = new StringBuffer();
                        generatedSourceBuf.append(CODE_HEADING);
                        srcFileName = c.getPosition().file().getAbsolutePath();

                        // Now, found all class's fields
                        // and for each field, find field annotated with @BooleanMagic
                        // and generating code base on @BooleanMagic properties
                        //
                        for (FieldDeclaration field : c.getFields()) {
                            for (AnnotationMirror fieldAnnotationMirror : field.getAnnotationMirrors()) {
                                if (fieldAnnotationMirror.getAnnotationType().getDeclaration().equals(jpaBooleanDeclaration)) {
                                    booleanMagicAnnotationFound = true;
                                    if (!verifyBooleanFieldOK(field)) {
                                        env.getMessager().printError(field.getPosition(), "Error!!" +
                                                c.getSimpleName() + "." +
                                                field.getSimpleName() +
                                                " does not modified with transient modifier or annotated with @ Transient annotation");
                                        return;
                                    }
                                    annotatedFieldList.add(field);
                                    System.out.println("Generated additional boolean properties source for:" + c.getSimpleName() + "." + field.getSimpleName());
                                    generatedSourceBuf.append(generateBooleanFieldSource(field, fieldAnnotationMirror) + NEW_LINE);
                                }
                            }
                        }

                        // Start generate source code base on annotated fields
                        // 
                        writer = env.getFiler().createSourceFile(c.getPackage().getQualifiedName() + "." + c.getSimpleName());
                        if (booleanMagicAnnotationFound) {
                            ArrayList<Integer> fieldMethodIndexArray = new ArrayList<Integer>();

                            // Find out all getter and setter for the annotated field from the original source code
                            // And remark it.
                            for (FieldDeclaration annotatedField : annotatedFieldList) {
                                String annotatedFieldName = StringUtils.capitalize(annotatedField.getSimpleName());
                                for (MethodDeclaration fieldMethod : c.getMethods()) {
                                    if (fieldMethod.getSimpleName().equals("get" + annotatedFieldName) ||
                                            fieldMethod.getSimpleName().equals("is" + annotatedFieldName) ||
                                            fieldMethod.getSimpleName().equals("set" + annotatedFieldName)) {
                                        fieldMethodIndexArray.add(fieldMethod.getPosition().line());
                                    }
                                }
                            }

                            StringBuffer buf = new StringBuffer();
                            BufferedReader bufReader = null;
                            String line = null;
                            StringBuffer tempBuf = null;
                            boolean firstImportFound = false;
                            try {
                                bufReader = new BufferedReader(new FileReader(c.getPosition().file()));
                                int lineNo = 1;
                                while ((line = bufReader.readLine()) != null) {

                                    // if the line match with the field setter and getter, 
                                    // put it on temporaryBuffer
                                    //
                                    if (fieldMethodIndexArray.contains(lineNo)) {
                                        tempBuf = new StringBuffer();
                                        int startLineIndex = methodLineArray.indexOf(lineNo);
                                        boolean theLastMethod = false;

                                        // Check if the method is last method.
                                        // if yes, read all the line till EOF.
                                        if (startLineIndex == methodLineArray.size() - 1) {
                                            while (line != null) {
                                                tempBuf.append(line + NEW_LINE);
                                                line = bufReader.readLine();
                                            }
                                            theLastMethod = true;
                                        } else {
                                            int endLine = methodLineArray.get(startLineIndex + 1) - 1;
                                            for (int i = lineNo; i < endLine; i++) {
                                                tempBuf.append(line + NEW_LINE);
                                                line = bufReader.readLine();
                                                lineNo++;
                                            }
                                        }
                                        
                                        int endMethodIndex = theLastMethod ? tempBuf.lastIndexOf("}", tempBuf.length() - 3) : tempBuf.lastIndexOf("}");
                                        buf.append(CODE_REMARK_START);
                                        buf.append(tempBuf.substring(0, endMethodIndex + 1));
                                        buf.append(CODE_REMARK_END);
                                        buf.append(tempBuf.substring(endMethodIndex + 1));
                                    }
                                    if (line == null) {
                                        break;
                                    }
                                    
                                    // Copy line by line from the source code, unremark the @BooleanMagicAnnotation
                                    if (!line.contains("@BooleanMagic")) {
                                        buf.append(line + NEW_LINE);
                                    }

                                    if (!firstImportFound && line.startsWith("import ")) {
                                        firstImportFound = true;
                                        // Ensure the VO have the javax.persistence.Column import
                                        buf.append("import javax.persistence.Column;" + NEW_LINE + NEW_LINE);
                                    }
                                    lineNo++;
                                }
                                bufReader.close();
                            } catch (Exception e) {
                            }
                            buf.insert(buf.lastIndexOf("}"), NEW_LINE + NEW_LINE + generatedSourceBuf.toString() + NEW_LINE + CODE_TAIL + NEW_LINE);
                            sourceFileText = buf.toString();
                        } else {
                            sourceFileText = getSourceCode(c.getPosition().file());
                        }

                        writer.append(sourceFileText);
                        writer.close();
                    }

                }


            } catch (IOException ex) {
            }

        }

        private String generateBooleanFieldSource(FieldDeclaration field, AnnotationMirror fieldAnnotationMirror) {
            String trueValue = null;
            String columnName = null;
            String falseValue = null;
            Boolean ifNullReturnValue = Boolean.FALSE;

            StringBuffer buf = new StringBuffer();
            String fieldName = StringUtils.capitalize(field.getSimpleName());

            Map<AnnotationTypeElementDeclaration, AnnotationValue> values = fieldAnnotationMirror.getElementValues();

            for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> entry : values.entrySet()) {
                AnnotationTypeElementDeclaration elemDecl = entry.getKey();
                AnnotationValue value = entry.getValue();
                if (elemDecl.getSimpleName().equals(BooleanMagic.TRUE_VALUE_KEY)) {
                    trueValue = (String) value.getValue();
                } else if (elemDecl.getSimpleName().equals(BooleanMagic.FALSE_VALUE_KEY)) {
                    falseValue = (String) value.getValue();
                } else if (elemDecl.getSimpleName().equals(BooleanMagic.COLUMN_NAME_KEY)) {
                    columnName = (String) value.getValue();
                } else if (elemDecl.getSimpleName().equals(BooleanMagic.IF_NULL_KEY)) {
                    ifNullReturnValue = (Boolean) value.getValue();
                }
            }


            buf.append("\t@Column(name=\"" + columnName + "\")" + NEW_LINE);
            buf.append("\tprivate String magicBoolean" + fieldName + ";" + NEW_LINE);
            buf.append(NEW_LINE + NEW_LINE);
            
            buf.append("\tpublic Boolean is" + fieldName + "() {" + NEW_LINE);
            buf.append("\t    if (this.magicBoolean" + fieldName + " == null) " + NEW_LINE);
            buf.append("\t        return " + ifNullReturnValue + ";" + NEW_LINE);
            buf.append("\t    return this.magicBoolean" + fieldName + ".equals(\"" + trueValue + "\") ? Boolean.TRUE : Boolean.FALSE;" + NEW_LINE);
            buf.append("\t}" + NEW_LINE + NEW_LINE);

            buf.append("\tpublic Boolean get" + fieldName + "() {" + NEW_LINE);
            buf.append("\t    if (this.magicBoolean" + fieldName + " == null) " + NEW_LINE);
            buf.append("\t        return " + ifNullReturnValue + ";" + NEW_LINE);
            buf.append("\t    return this.magicBoolean" + fieldName + ".equals(\"" + trueValue + "\") ? Boolean.TRUE : Boolean.FALSE;" + NEW_LINE);
            buf.append("\t}" + NEW_LINE + NEW_LINE);
            
            buf.append("\tpublic void set" + fieldName + "(Boolean trueFlag) {" + NEW_LINE);
            buf.append("\t     this.magicBoolean" + fieldName + " = trueFlag ? \"" + trueValue + "\" : \"" + falseValue + "\";" + NEW_LINE);
            buf.append("\t} " + NEW_LINE + NEW_LINE);


            return buf.toString();
        }

        private String getSourceCode(File file) {
            StringBuffer buf = new StringBuffer();
            String line = null;
            BufferedReader bufReader = null;
            try {
                bufReader = new BufferedReader(new FileReader(file));
                while ((line = bufReader.readLine()) != null) {
                    buf.append(line + NEW_LINE);
                }
                bufReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return buf.toString();
        }

        private boolean verifyBooleanFieldOK(FieldDeclaration field) {
            boolean fieldOK = true;
            TypeDeclaration transientAnnotation = env.getTypeDeclaration(BooleanMagicProcessorFactory.JAVAX_PERSISTANCE_TRANSIENT_ANNOTATION);
            boolean transientModifierFound = false;
            boolean transientAnnotationFound = false;
            // Needs to check if the field modifie
            for (Modifier modify : field.getModifiers()) {
                if (modify == Modifier.TRANSIENT) {
                    transientModifierFound = true;
                }
            }

            for (AnnotationMirror mirror : field.getAnnotationMirrors()) {
                if (mirror.getAnnotationType().getDeclaration().equals(transientAnnotation)) {
                    transientAnnotationFound = true;
                }
            }
            if (!(transientAnnotationFound || transientModifierFound)) {
                fieldOK = false;
            }
            return fieldOK;
        }
    }
}
