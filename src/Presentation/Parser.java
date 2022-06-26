package Presentation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Parser {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    private static final HashMap<String, Integer> getMonthNumber = new HashMap() {
        {
            put("Jan", 1);
            put("Feb", 2);
            put("Mar", 3);
            put("Apr", 4);
            put("May", 5);
            put("Jun", 6);
            put("Jul", 7);
            put("Aug", 8);
            put("Sep", 9);
            put("Oct", 10);
            put("Nov", 11);
            put("Dec", 12);
        }
    };

    public static void handleAction(Action a){
        if(a == null){
            printInvalidInput();
        }
        else{
            a.act();
        }
    }
    public static void printInvalidInput(){
        System.out.println("invalid input");
    }


    private static final Scanner scanner = new Scanner(System.in);

    public static String getStrDate(Date date){
        return simpleDateFormat.format(date);
    }

    public static Date getDate(String date) {
        try {
            return simpleDateFormat2.parse(date);
        } catch (Exception e) {
            try {
                return simpleDateFormat.parse(date);
            }
            catch (Exception ex) {
                String[] in = date.split(" ");
                if (in.length > 2) {
                    try {
                        return simpleDateFormat.parse(in[2] + "-" + getMonthNumber.get(in[1]) + "-" + in[0]);
                    } catch (ParseException exp) {
                        try {
                            return simpleDateFormat.parse(in[2] + "-" + getMonthNumber.get(in[1]) + "-" + in[5]);
                        }
                        catch (Exception exc){
                            System.out.println("error occurred");
                        }
                    }
                }

                return new Date();
            }
        }
    }
    public static Date getComplexDate(String date) {
        try {
            return simpleDateFormat1.parse(date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String getStrInput(String msg) {
        try {
            System.out.println(msg);
            String input = scanner.nextLine();
            while (input.equals("")) {
                input = scanner.nextLine();
            }
            return input;
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getStrInput(msg);
        }
    }

    public static String getStrInput() {
        try {
            String input = scanner.nextLine();
            while (input.equals("")) {
                input = scanner.nextLine();
            }
            return input;
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getStrInput();
        }
    }

    public static int getIntInput(String msg) {
        try {
            return Integer.parseInt(getStrInput(msg));
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getIntInput(msg);
        }
    }

    public static double getDoubleInput(String msg) {
        try {
            return Double.parseDouble(getStrInput(msg));
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getDoubleInput(msg);
        }
    }

    public static Date getDateInput(String msg) {
        try {
            String input = getStrInput(msg);
            return simpleDateFormat.parse(input);
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getDateInput(msg);
        }
    }
    public static Date getComplexDateInput(String msg) {
        try {
            String input = getStrInput(msg);
            return simpleDateFormat1.parse(input);
        } catch (Exception e) {
            System.out.println("error occurred, try again");
            return getComplexDateInput(msg);
        }
    }

    public static <T> String printList(List<T> lst) {
        return printList(lst, Object::toString);
    }

    public static <T> String printList(List<T> lst, Function<T, String> changeVal) {
        return lst.stream().map(t -> changeVal.apply(t) + "\n").collect(Collectors.joining());
    }

}
