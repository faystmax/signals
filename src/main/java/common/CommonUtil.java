package common;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 13.10.2018
 */
public class CommonUtil {

    public static String convertSecondsToHMmSs(long mills) {
        long second = (mills / 1000) % 60;
        long minute = (mills / (1000 * 60)) % 60;
        long hour = (mills / (1000 * 60 * 60)) % 24;

        return String.format("%02dч %02dм %02dс %04dмс", hour, minute, second, mills);
    }
}
