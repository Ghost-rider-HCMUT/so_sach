package com.sotraubo.app.core.common.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Bộ định dạng tiền tệ VNĐ (sử dụng kiểu Long tuyệt đối không sai số)
 * và đọc số tiền thành chữ tiếng Việt tự nhiên cho hộ kinh doanh.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u000f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eJ\u0016\u0010\u0010\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0005J\u0018\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0012H\u0002J\u000e\u0010\u001a\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eR\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/sotraubo/app/core/common/format/CurrencyFormatter;", "", "()V", "digitWords", "", "", "[Ljava/lang/String;", "numberFormat", "Ljava/lang/ThreadLocal;", "Ljava/text/DecimalFormat;", "vietnameseSymbols", "Ljava/text/DecimalFormatSymbols;", "formatCompactVnd", "amount", "", "formatNumberOnly", "formatSignedVnd", "isPositive", "", "formatVnd", "parseDigitsToLong", "rawInput", "readThreeDigits", "number", "", "readZeroHundred", "toVietnameseWords", "app_release"})
public final class CurrencyFormatter {
    @org.jetbrains.annotations.NotNull()
    private static final java.text.DecimalFormatSymbols vietnameseSymbols = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.ThreadLocal<java.text.DecimalFormat> numberFormat = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] digitWords = {"kh\u00f4ng", "m\u1ed9t", "hai", "ba", "b\u1ed1n", "n\u0103m", "s\u00e1u", "b\u1ea3y", "t\u00e1m", "ch\u00edn"};
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.core.common.format.CurrencyFormatter INSTANCE = null;
    
    private CurrencyFormatter() {
        super();
    }
    
    /**
     * Định dạng số nguyên Long sang chuỗi tiền tệ có hậu tố " đ".
     * Ví dụ: 30000000L -> "30.000.000 đ"
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatVnd(long amount) {
        return null;
    }
    
    /**
     * Định dạng có dấu cộng/trừ rõ ràng cho dòng tiền Nhận/Chuyển.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatSignedVnd(long amount, boolean isPositive) {
        return null;
    }
    
    /**
     * Định dạng chỉ lấy phần số có dấu chấm phân cách (dùng trong ô nhập liệu).
     * Ví dụ: 30000000L -> "30.000.000"
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatNumberOnly(long amount) {
        return null;
    }
    
    /**
     * Chuyển chuỗi người dùng nhập (có thể chứa dấu chấm, khoảng trắng) về số nguyên Long.
     */
    public final long parseDigitsToLong(@org.jetbrains.annotations.NotNull()
    java.lang.String rawInput) {
        return 0L;
    }
    
    /**
     * Định dạng rút gọn trên trục biểu đồ (Ví dụ: 30.000.000 -> "30 tr", 1.500.000 -> "1,5 tr").
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatCompactVnd(long amount) {
        return null;
    }
    
    /**
     * Đọc số tiền thành chữ tiếng Việt để chủ hộ kiểm tra nhanh tránh nhập thừa/thiếu số 0.
     * Ví dụ: 30000000L -> "Ba mươi triệu đồng chẵn"
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toVietnameseWords(long amount) {
        return null;
    }
    
    private final java.lang.String readThreeDigits(int number, boolean readZeroHundred) {
        return null;
    }
}