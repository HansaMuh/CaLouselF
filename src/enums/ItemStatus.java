package enums;

public enum ItemStatus {
    PENDING, // Default status, when item is newly created by seller
    APPROVED, // When item is approved by admin
    DECLINED, // When item is declined by admin
    SOLD_OUT, // When item is sold out from getting purchased or confirmed offer by buyer

    INVALID // When item is invalid from getting deleted by seller
}
