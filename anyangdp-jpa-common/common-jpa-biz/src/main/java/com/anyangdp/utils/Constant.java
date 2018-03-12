package com.anyangdp.utils;

/**
 * 常量
 */
public class Constant {

    public static final String USER_VERIFY_CODE = "user:verifycode:";
    public static final String REQUEST_MAPPING_URLS = "request_mapping_urls";

	//菜单顶级id
	public static final Long MENU_TOP_ID = 0l;
	
	public static final String CURRENT_EXTRA_KEY = "user:extra:";
	public static final String USER_COMPANY = "company";
	public static final String USER_COMPANY_LIGHT = "company_light";


    /**
	 * 菜单类型
	 */
    public enum MenuType {
    	/** 菜单 */
        MENU(0),
    	/** 目录 */
    	CATALOG(1);
        public Integer value;
        private MenuType(Integer value) {
            this.value = value;
        }
    }

}
