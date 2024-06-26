package com.PosSystem.POS.System.Utils;

public class Messages {
    //GENERAL
    public static final String FAIL_CODE = "GEN_001";
    public static final String FAIL_MESSAGE = "Operation failed";
    public static final String SUCCESS_CODE = "GEN_002";
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String CREATED_CODE = "GEN_003";
    public static final String CREATED_MESSAGE = "Created";
    public static final String UPDATED_CODE = "GEN_004";
    public static final String UPDATED_MESSAGE = "Updated";
    public static final String DELETED_CODE = "GEN_005";
    public static final String DELETED_MESSAGE = "Deleted";
    public static final String EMPTY_CODE = "GEN_006";
    public static final String EMPTY_MESSAGE = "No objects found";
    public static final String ALREADY_EXISTS_CODE = "GEN_007";
    public static final String ALREADY_EXISTS_MESSAGE = "Already exists";
    public static final String INVALID_INPUT_CODE = "GEN_008";
    public static final String INVALID_INPUT_MESSAGE = "Invalid Input";
    public static final String DOES_NOT_EXIST_CODE = "GEN_009";
    public static final String DOES_NOT_EXIST_MESSAGE = "Does not exist";

    //USER
    public static final String EMAIL_EXISTS_CODE = "USR_001";
    public static final String EMAIL_EXISTS_MESSAGE = "Email already exists";
    public static final String ACCOUNT_CREATED_CODE = "USR_002";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account successfully created";

    //TOPPINGS
    public static final String TOPPING_DOES_NOT_EXIST_CODE = "TP_001";
    public static final String TOPPING_DOES_NOT_EXIST_MESSAGE = "Topping does not exist";

    //ORDER
    public static final String ORDER_ITEM_ADDED_CODE = "ORD_001";
    public static final String ORDER_ITEM_ADDED_MESSAGE = "Order item added";

    //CHECKOUT
    public static final String ORDER_IS_EMPTY_CODE = "CHK_001";
    public static final String ORDER_IS_EMPTY_MESSAGE = "Cannot checkout. Order is empty";

    //STRIPE
    public static final String STRIPE_ERROR_CODE = "STRP_001";
    public static final String STRIPE_ERROR_MESSAGE = "Something went wrong with Stripe";


}
