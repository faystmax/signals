import lab1.App1Lab;
import lab2.App2Lab;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            log.error("No argument! Pls specify what lab u want to start! (lab1,lab2... etc)");
            return;
        }
        String lab = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        if ("lab1".equals(lab)) {
            new App1Lab().show(args);
        } else if ("lab2".equals(lab)) {
            new App2Lab().show(args);
        }
    }
}
