package org.jbpcc.admin.constants;


/**
 * Error keys are enumerations for message properties that correspond to error messages.
 */
public enum ErrorMessageKey {

    LOGIN_INVALID("err.login.invalidLogin"),
    MAXIMUM_LOGIN_EXCEEDED("err.login.maximumLoginExceeded"),
    MAXIMUM_LOGIN_ATTEMPTS("err.login.maximumLoginAttempts"),
    USER_ALREADY_LOCKED("err.login.userAlreadyLocked"),
    TEAM_DISABLED("err.login.teamHasBeenDisabled"),
    USER_DISABLED("err.login.userHasBeenDisabled"),
    NP_ACCESS_RIGHTS("err.login.noRights"),
    LOGIN_AUTHENTICATION_FAILED("err.login.authenticationFailed"),

    UNKNOWN_VALIDATION_ERROR("err.common.validation.unknown"),

    INVALID_PORT("err.common.validation.invalidport"),

     // JMX error keys
    JMX_LOGIN_ERROR("err.jmx.client.login.error"),
    JMX_CONNECTION_ERROR("err.jmx.client.connection.error"),
    JMX_RETRIEVE_OBJ_ERROR("err.jmx.client.retrieve.object.attribute.error"),
    JMX_INVOKE_METHOD_ERROR("err.jmx.client.invoke.method.error"),

    // Password validation error keys
    PASSWORD_MISSING("err.password.validation.missing"),
    PASSWORD_MISMATCH("err.password.validation.mismatch"),
    PASSWORD_NO_WHITESPACE("err.password.validation.nowhitespace"),

    // User Management error keys
    USER_CREATE_FAILED("err.user.create.failed"),
    USER_UPDATE_FAILED("err.user.update.failed"),
    USER_DELETE_FAILED("err.user.delete.failed"),
    USER_LOGIN_NAME_EXISTS("err.user.validation.loginexists"),
    USER_LOGIN_NAME_INVALID("err.user.validation.logininvalid"),
    USER_MISSING_REQUIRED_FIELD("err.user.validation.missingrequired"),

    // Server Configuration error keys
    SERVER_CREATE_FAILED("err.server.create.failed"),
    SERVER_UPDATE_FAILED("err.server.update.failed"),
    SERVER_DELETE_FAILED("err.server.delete.failed"),
    SERVER_MISSING_REQUIRED_FIELD("err.server.validation.missingrequired"),
    SERVER_HAS_JOB_DEPENDENCY("err.server.validation.jobdependency"),


    // Scheduler error keys
    SCHEDULER_CREATE_FAILED("err.scheduler.create.failed"),
    SCHEDULER_UPDATE_FAILED("err.scheduler.update.failed"),
    SCHEDULER_DELETE_FAILED("err.scheduler.delete.failed"),
    SCHEDULER_SCHEDULE_FAILED("err.scheduler.schedule.failed"),
    SCHEDULER_UNSCHEDULE_FAILED("err.scheduler.unschedule.failed"),
    SCHEDULER_VALIDATE_JOB_NAME_EMPTY("err.scheduler.validation.jobnameempty"),
    SCHEDULER_VALIDATE_JOB_OPR_EMPTY("err.scheduler.validation.joboperationempty"),
    SCHEDULER_VALIDATE_STARTDATE_EMPTY("err.scheduler.validation.startdateempty"),
    SCHEDULER_VALIDATE_STARTTIME_EMPTY("err.scheduler.validation.starttimeempty"),
    SCHEDULER_VALIDATE_STARTTIME_ERROR("err.scheduler.validation.starttimeerror"),
    SCHEDULER_VALIDATE_STOPDATE_EMPTY("err.scheduler.validation.stopdateempty"),
    SCHEDULER_VALIDATE_STOPTIME_ERROR("err.scheduler.validation.stoptimeerror"),
    SCHEDULER_VALIDATE_DATERANGE_ERROR("err.scheduler.validation.daterangeerror"),
    SCHEDULER_VALIDATE_INTERVAL_ERROR("err.scheduler.validation.intervalerror"),
    SCHEDULER_VALIDATE_CRONEXP_EMPTY("err.scheduler.validation.cronexpempty"),
    SCHEDULER_VALIDATE_CRONEXP_ERROR("err.scheduler.validation.cronexperror");

    private String key;

    private ErrorMessageKey(String errKey) {
        this.key = errKey;
    }

    public String getKey() {
        return key;
    }
}
