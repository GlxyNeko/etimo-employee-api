package com.etimo.etimo_employee_api.controller;

public final class ApiEndpoints {
        private ApiEndpoints() {}

        public static final String API_BASE = "/api";
        public static final String V1 = "/v1";
        public static final String EMPLOYEES = API_BASE + V1 + "/employees";

        public static final String ID = "id";
        public static final String BY_ID = "/{"+ ID + "}";
}
