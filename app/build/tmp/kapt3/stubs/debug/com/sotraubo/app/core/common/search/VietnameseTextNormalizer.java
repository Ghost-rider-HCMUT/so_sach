package com.sotraubo.app.core.common.search;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Bộ chuẩn hóa chuỗi tiếng Việt phục vụ tìm kiếm không dấu và chuẩn hóa số điện thoại.
 * Ví dụ: "Nguyễn Văn Đạt" -> "nguyen van dat"
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\r\u001a\u00020\u000bJ\u000e\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bJ\u0010\u0010\u0010\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/sotraubo/app/core/common/search/VietnameseTextNormalizer;", "", "()V", "DIACRITICS_REGEX", "Lkotlin/text/Regex;", "MULTIPLE_SPACES_REGEX", "NON_DIGIT_REGEX", "PHONE_QUERY_REGEX", "matchesQuery", "", "fullName", "", "phone", "rawQuery", "normalizeName", "input", "normalizePhone", "app_debug"})
public final class VietnameseTextNormalizer {
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex DIACRITICS_REGEX = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex MULTIPLE_SPACES_REGEX = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex NON_DIGIT_REGEX = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex PHONE_QUERY_REGEX = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.core.common.search.VietnameseTextNormalizer INSTANCE = null;
    
    private VietnameseTextNormalizer() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String normalizeName(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String normalizePhone(@org.jetbrains.annotations.Nullable()
    java.lang.String phone) {
        return null;
    }
    
    /**
     * Khớp theo TỪNG TỪ (không phụ thuộc thứ tự, không dấu, không phân biệt hoa thường):
     * "dat nguyen" vẫn tìm thấy "Nguyễn Văn Đạt".
     * Chỉ so với số điện thoại khi ô tìm kiếm thuần là số (tránh khớp nhầm "Hai 2").
     */
    public final boolean matchesQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.Nullable()
    java.lang.String phone, @org.jetbrains.annotations.NotNull()
    java.lang.String rawQuery) {
        return false;
    }
}