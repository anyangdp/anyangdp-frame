package com.anyangdp.utils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Random;

/**
 * 主键生存器
 * 
 * @author
 * @time
 * @version 1.0
 */
public class PrimaryKey {

	private static Object primaryKeyLock = new Object();// 锁对象
	private static Random random = new Random(System.currentTimeMillis());

	// 内部计算器 5位16进制可到100万
	private static long counter = (new Random(System.currentTimeMillis())).nextLong() & 0xFFFFF;
	static {
		System.out.println("Primary key counter init = " + counter);
	}
	// 应用服务器Id 一位字符标识
	private static final String appServerIds = "ABCDEFGHIJKLMNPRSTVWXYZ";// 应用服务器ID范围
	private static char appServerId = appServerIds.charAt((int) (counter % appServerIds.length()));

	// 临时计数器，用于系统中临时取得一个唯一值时候用
	private static long tempCounter = (new Random(System.currentTimeMillis())).nextLong() & 0xFFFFF;

	private static final String zero = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	/**
	 * 36位16进制的UUID 带有连接符-
	 * 
	 * @return
	 */
	public static String getUuid() {
		java.util.UUID uuid = java.util.UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * 
	 * @return 32位16进制的UUID,字母为大写
	 */
	public static String getUuidToUpperCase() {
		StringBuffer sb = new StringBuffer(32);
		String id = getUuid();
		char ch;
		for (int i = 0, n = id.length(); i < n; i++) {
			ch = id.charAt(i);
			if (ch <= '9' && ch >= '0') // 48-57
				sb.append(ch);
			else if (ch >= 'a' && ch <= 'f')// 97-...
				sb.append((char) (ch - 32));
			else if (ch >= 'A' && ch <= 'F')// 65-...
				sb.append(ch);
		}

		return sb.toString();
	}

	/**
	 * 
	 * @return 32位16进制的UUID,字母为小写
	 */
	public static String getUuidToLowerCase() {
		StringBuffer sb = new StringBuffer(32);
		String id = getUuid();
		char ch;
		for (int i = 0, n = id.length(); i < n; i++) {
			ch = id.charAt(i);
			if (ch <= '9' && ch >= '0') // 48-57
				sb.append(ch);
			else if (ch >= 'a' && ch <= 'f')// 97-...
				sb.append(ch);
			else if (ch >= 'A' && ch <= 'F')// 65-...
				sb.append((char) (ch + 32));
		}

		return sb.toString();
	}

	/**
	 * 
	 * @return 返回长度为23位的字符串 YYYYMMDDHHMMSSZZZ+5位内部计数器+1位应用服务器ID = 23位
	 */
	public static String getPrimaryKey() {
		return getPrimaryKey23();
	}

	/**
	 * 
	 * @return 图片ID 23位长度
	 */
	public static String getPrimaryKeyPicture() {
		return getPrimaryKey23();
	}

	/**
	 * 
	 * @return 返回长度为23位的字符串 YYYYMMDDHHMMSSZZZ+5位内部计数器+1位应用服务器ID = 23位
	 */
	public static String getPrimaryKey23() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		// System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS")
		// .format(new java.util.Date()));
		String count = "00000" + Long.toHexString(next()).toUpperCase();
		// System.out.println(count);
		sb.append(count.substring(count.length() - 5));
		// System.out.println(appServerId);
		sb.append(appServerId);

		return sb.toString();
	}

	public static String getPrimaryKey4() {
		StringBuffer sb = new StringBuffer(4);
		String count = "00000" + Long.toHexString(next()).toUpperCase();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * 
	 * @return 返回长度为25位的字符串 YYYYMMDDHHMMSSZZZ+5位内部计数器+2位随机值+1位应用服务器ID = 25位
	 */
	public static String getPrimaryKey25() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));

		String count = "00000" + Long.toHexString(next()).toUpperCase();
		sb.append(count.substring(count.length() - 5));

		count = "0" + Long.toHexString(random.nextLong() & 0xFF).toUpperCase();
		sb.append(count.substring(count.length() - 2));
		sb.append(appServerId);

		return sb.toString();
	}

	/**
	 * 
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+3位内部计数器+1位应用服务器ID = 20位
	 */
	public static String getPrimaryKey20() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "00000" + Long.toHexString(next()).toUpperCase();
		sb.append(count.substring(count.length() - 3));
		sb.append(appServerId);

		return sb.toString();
	}

	/**
	 * 
	 * @return 计数器和应用服务器ID形成的字符串,不足6位前加0
	 */
	public static String getCounterKey() {
		StringBuffer sb = new StringBuffer(11);
		sb.append("00000").append(Long.toHexString(next()).toUpperCase()).append(appServerId);

		return sb.substring(sb.length() - 6);
	}

	/**
	 * 
	 * @return 得到下一个内部计数值
	 */
	private static long next() {
		synchronized (primaryKeyLock) {
			counter++;
			counter = counter & 0xFFFFF;

			return counter;
		}
	}

	/**
	 * 
	 * @return 返回应用服务器ID
	 */
	public static char getAppServerId() {
		return appServerId;
	}

	/**
	 * 刷新系统配置数据
	 * 
	 * @return 刷新是否成功
	 */
	public synchronized static boolean refresh() {
		String appId = null;// 应用服务器ID

		InputStream is = null;
		boolean result = true;
		System.out.println("Primary key config start...");
		try {
			is = new PrimaryKey().getClass().getResourceAsStream("/ds.properties");
			Properties dbProps = new Properties();
			dbProps.load(is);
			appId = dbProps.getProperty("AppServerId");
			is.close();
			is = null;
		} catch (Exception ex) {
			System.out.println("系统文件读取错误(property)");
			result = false;
			ex.printStackTrace();
		}

		if (!result) {
			System.out.println("Primary key aborted!");
			return false;
		}

		// 处理更新
		if (appId == null || appId.length() <= 0 || appId.equals("null"))
			appId = ",";
		else
			appId = appId.toUpperCase().substring(0, 1);

		if (appServerIds.indexOf(appId) >= 0)
			appServerId = appId.charAt(0);
		else
			appServerId = appServerIds.charAt((int) (counter % appServerIds.length()));

		System.out.println("App server id = " + appServerId);

		System.out.println("Primary key succeed! end.");

		return true;
	}

	/**
	 * 
	 * @return 临时计数器
	 */
	public static long getTempCounterKey() {
		return tempNext();
	}

	/**
	 * 
	 * @return 临时计数器加1后的值
	 */
	private static long tempNext() {
		synchronized (primaryKeyLock) {
			tempCounter++;
			tempCounter = tempCounter & 0xFFFFF;

			return tempCounter;
		}
	}

	/**
	 * 得到顺序号的字符串，前加0, 最长108位
	 * 
	 * @param orderNumber
	 *            顺序号 如果是负数按绝对值
	 * @param length
	 *            返回字符串总长度 如果是小于等于1按4算，如果超过108按108算
	 * @return
	 */
	public static String getSeq(int orderNumber, int length) {
		if (orderNumber < 0)
			orderNumber = -orderNumber;

		if (length <= 1)
			length = 4;
		if (length > 108)
			length = 108;

		String result = zero.substring(0, length) + orderNumber;
		return result.substring(result.length() - length);
	}

	/**
	 * 得到顺序号的字符串，前加0, 4位长度
	 * 
	 * @param orderNumber
	 *            顺序号 如果是负数按绝对值
	 * @return
	 */
	public static String getSeq4(int orderNumber) {
		if (orderNumber < 0)
			orderNumber = -orderNumber;

		String result = zero.substring(0, 4) + orderNumber;
		return result.substring(result.length() - 4);
	}

	/**
	 * @描述 生成出入库流水号
	 * @author KCJ
	 * @Date 2013年2月19日11:23:39
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+4位内部计数器 = 20位
	 */
	public static String getOutInId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成采购申请流水号
	 * @author wangqm
	 * @return 返回长度为22位的字符串cg YYYYMMDDHHMMSSZZ+4位内部计数器 = 22位
	 */
	public static String getPurNo() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("cg");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * @描述 生成借用归还流水号
	 * @author KCJ
	 * @Date 2013年2月19日11:23:39
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+4位内部计数器 = 20位
	 */
	public static String getBrrRetId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成成套流水号
	 * @author liaobin
	 * @Date 2013年2月19日11:23:39
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+4位内部计数器 = 20位
	 */
	public static String getSuitId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成借用ID
	 * @author KCJ
	 * @Date 2013年2月19日11:23:39
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+4位内部计数器 = 20位
	 */
	public static String getApplyId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 库存变动流水ID
	 * @author KCJ
	 * @Date 2013年2月19日11:23:39
	 * @return 返回长度为22位的字符串 YYYYMMDDHHMMSSZZZ+5位内部计数器 = 22位
	 */
	public static String getStoreChangeId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));

		String count = "00000" + next();
		sb.append(count.substring(count.length() - 5));

		return sb.toString();
	}

	/**
	 * @描述 生成调拨流水号
	 * @author ICE
	 * @return 返回长度为22位的字符串db YYYYMMDDHHMMSSZZ+4位内部计数器 = 22位
	 */
	public static String getAllotNo() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("db");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * @描述 生成盘点流水号
	 * @author ICE
	 * @return 返回长度为22位的字符串pd YYYYMMDDHHMMSSZZ+4位内部计数器 = 22位
	 */
	public static String getCheckNo() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("pd");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	public static String getOverdueId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("gb");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	public static String getImageId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("i");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * 生成待处理任务单号
	 * 
	 * @return
	 */
	public static String getTaskNo() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * 生成操作日志单号
	 * 
	 * @return
	 */
	public static String getOperateId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * 银联支付订单号生成，16位
	 * 
	 * @return 返回长度为16位的字符串 YYYYMMDDHHMM+next
	 * @author ICE
	 * @date 2014-8-13 上午11:11:54
	 */
	public static String getOrderNo() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date()));

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * 管理员订购时长标识ID
	 * 
	 * @return
	 * @author ICE
	 * @date 2014-8-21 下午02:56:15
	 */
	public static String getAdminDuraNo() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date()));

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * 员工订购时长标识ID
	 * 
	 * @return
	 * @author ICE
	 * @date 2014-8-21 下午02:56:15
	 */
	public static String getStaffDuraNo() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date()));

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * @描述 生成清空申请ID号
	 * @author hmh
	 * @return 返回长度为22位的字符串qk YYYYMMDDHHMMSSZZ+4位内部计数器 = 22位
	 */
	public static String getApplyEmptyId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("qk");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * @描述 生成积分流水号
	 * @author hmh
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+4位内部计数器 = 20位
	 */
	public static String getPointsInfoId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成生产流水号
	 * @author hmh
	 * @return 返回长度为22位的字符串sc YYYYMMDDHHMMSSZZ+4位内部计数器 = 22位
	 */
	public static String getProductionId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("sc");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * 生成积分兑换历史ID
	 * 
	 * @author ICE
	 * @date 2014-11-7 下午05:39:06
	 */
	public static String getScoreHistoryId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * 询价单据ID
	 * 
	 * @author ICE
	 * @date 2014-11-14 下午02:28:47 返回长度为22位的字符串xj YYYYMMDDHHMMSSZZ+4位内部计数器 = 22位
	 */
	public static String getEnquiryNo() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("xj");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));
		return sb.toString();
	}

	/**
	 * @描述 生成质保流水号
	 * @author hmh
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+4位内部计数器 = 20位
	 */
	public static String getQualityId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成PDM流水号
	 * @author hmh
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+4位内部计数器 = 20位
	 */
	public static String getPdmId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成商城订单号
	 * @author hailin
	 * @return 返回长度为17位的订单号
	 */
	public static String getOrderId() {
		StringBuffer sb = new StringBuffer();
		// 生成两位随机数
		String rs1 = getRandom(10, 99);
		// 生成三位随机数
		String rs2 = getRandom(100, 999);
		sb.append(rs1);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append(rs3.toString().substring(1));
		sb.append(rs2);

		return sb.toString();
	}

	/**
	 * @描述 生成商城子订单号
	 * @author hailin
	 * @return 返回长度为17位的子订单号
	 */
	public static String getChildOrderId() {
		StringBuffer sb = new StringBuffer();
		// 生成两位随机数
		String rs1 = getRandom(10, 99);
		// 生成三位随机数
		String rs2 = getRandom(100, 999);
		sb.append(rs1);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append(rs3.toString().substring(1));
		sb.append(rs2);

		return sb.toString();
	}

	/**
	 * @描述 生成临时商品表的订单号
	 * @author hailin
	 * @return 返回长度为17位的订单号
	 */
	public static String getTempId() {
		StringBuffer sb = new StringBuffer();
		// 生成两位随机数
		String rs1 = getRandom(10, 99);
		// 生成三位随机数
		String rs2 = getRandom(100, 999);
		sb.append(rs1);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append(rs3.toString().substring(1));
		sb.append(rs2);

		return sb.toString();
	}

	/**
	 * @描述 生成商城支付流水号
	 * @author hailin
	 * @return 返回长度为16位的字符串 MMDDHHMMSSZZ+4位内部计数器 = 16位
	 */
	public static String getPayId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("MMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成指定范围的随机数
	 * @param min
	 *            范围起始值
	 * @param max
	 *            范围终点值
	 * @author hailin
	 * @return 返回指定范围的随机数字符串
	 */
	public static String getRandom(int min, int max) {
		Random random = new Random();
		// 生成两位随机数
		int rs = random.nextInt(max) % (max - min + 1) + min;
		return String.valueOf(rs);

	}

	/**
	 * @描述 生成条形码
	 * @author hmh
	 * @return 返回长度为17位的字符串 YYYYMMDDHHMMSS+3位内部计数器 = 17位
	 */
	public static String getBarCode() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成临时的退货流水号
	 * @return 返回长度为20位的字符串 YYYYMMDDHHMMSSZZ+4位内部计数器 = 20位
	 */
	public static String getReturnId() {
		StringBuffer sb = new StringBuffer(32);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()));
		sb.deleteCharAt(sb.length() - 1);

		String count = "0000" + next();
		sb.append(count.substring(count.length() - 4));

		return sb.toString();
	}

	/**
	 * @描述 生成公告流水号
	 * @return 返回长度为9位的字符串 n+8位随机数
	 */
	public static String getNoticeId() {
		StringBuffer sb = new StringBuffer();
		// 生成三位随机数
		String rs1 = getRandom(100, 999);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append("n");
		sb.append(rs3.toString().substring(8));
		sb.append(rs1);

		return sb.toString();
	}

	/**
	 * @描述 生成部门流水号
	 * @return 返回长度为9位的字符串 b+8位随机数
	 */
	public static String getDeptId() {
		StringBuffer sb = new StringBuffer();
		// 生成三位随机数
		String rs1 = getRandom(100, 999);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append("b");
		sb.append(rs3.toString().substring(8));
		sb.append(rs1);

		return sb.toString();
	}

	/**
	 * @描述 生成供应商流水号
	 * @return 返回长度为10位的字符串 sp+8位随机数
	 */
	public static String getSupplyId() {
		StringBuffer sb = new StringBuffer();
		// 生成四位随机数
		String rs1 = getRandom(1000, 9999);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append("sp");
		sb.append(rs3.toString().substring(9));
		sb.append(rs1);

		return sb.toString();
	}

	/**
	 * @描述 生成人员流水号
	 * @return 返回长度为9位的字符串 y+8位随机数
	 */
	public static String getStaffId() {
		StringBuffer sb = new StringBuffer();
		// 生成三位随机数
		String rs1 = getRandom(100, 999);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append("y");
		sb.append(rs3.toString().substring(8));
		sb.append(rs1);

		return sb.toString();
	}

	/**
	 * 优惠码随机生成
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * @描述 生成公告流水号
	 * @return 返回长度为9位的字符串 n+8位随机数
	 */
	public static String getNatureId() {
		StringBuffer sb = new StringBuffer();
		// 生成三位随机数
		String rs1 = getRandom(100, 999);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append("x");
		sb.append(rs3.toString().substring(8));
		sb.append(rs1);

		return sb.toString();
	}

	/**
	 * @描述 生成柜子流水号
	 * @return 返回长度为9位的字符串 gz+7位随机数
	 */
	public static String getCabId() {
		StringBuffer sb = new StringBuffer();
		// 生成三位随机数
		String rs1 = getRandom(100, 999);
		// 获取当前的时间戳
		Long rs3 = System.currentTimeMillis();
		sb.append("gz");
		sb.append(rs3.toString().substring(9));
		sb.append(rs1);

		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(getCabId());
		System.out.println(getCabId());
		System.out.println(getCabId());
		System.out.println(getCabId());
		System.out.println(getCabId());
	}
}
