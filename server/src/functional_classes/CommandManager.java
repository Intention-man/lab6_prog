package functional_classes;

import auxiliary_classes.CommandMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandManager {

    public static <T> T execution(CommandMessage commandMessage) {

        String className = commandMessage.getClassname();
        System.out.println(className);
        Object o = null;
        try {
            Class<?> c = Class.forName("server.functional_classes." + className);
            Method methodToInvoke = c.getMethod(commandMessage.getCommandName());
            System.out.println(c.getName() + "." + methodToInvoke.getName());
            if (commandMessage.getCommandData() != null) {
                o = (T) methodToInvoke.invoke(c, commandMessage.getCommandData());
            } else {
                o = (T) methodToInvoke.invoke(c);
            }

            return (T) o;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | SecurityException |
                 IllegalArgumentException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
