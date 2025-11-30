package com.workastra.common.api;

public record Pagination(int page, int pageSize, long totalItems, int totalPages) {}
