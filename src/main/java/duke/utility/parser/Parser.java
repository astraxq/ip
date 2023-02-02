package duke.utility.parser;

import duke.duke_exception.DukeException;
import duke.tasklist.TaskList;
import duke.tasklist.task_types.Deadline;
import duke.tasklist.task_types.Event;
import duke.tasklist.task_types.Task;
import duke.tasklist.task_types.ToDo;
import duke.utility.ui.UiMessage;


/**
 * Represents a <code>Parser</code> object that perfroms the necessary
 * operations based on the user input.
 * 
 * @author Brian Quek
 */
public class Parser {
    private static DukeException wrongCommandFormat = new DukeException("Wrong command format inserted.");
    private static DukeException noNumericParam = new DukeException("Parameter is not a numerical value.");
    private static DukeException noSpecialParam = new DukeException("Missing special param e.g /by, /from, /to");
    private static DukeException emptyParam = new DukeException("Empty parameter inserted.");

    
    /** 
     * Prints the list of tasks stored in the TaskList.
     * @param command the command segments based on the user's input.
     * @param list the TaskList to be displayed.
     * @return UiMessage object containing what type of command to be printed out.
     * @throws DukeException if the command format is invalid.
     */
    private static UiMessage printTasks(String[] command, TaskList list) throws DukeException {
        if (command.length > 1) {
            throw wrongCommandFormat;
        }
        return new UiMessage(CommandMap.list, null);
    }

    
    /** 
     * Marks a task in the task list.
     * @param command the command segments based on the user's input.
     * @param list the list of tasks.
     * @return UiMessage object containing what type of command to print out.
     * @throws DukeException if the command format is invalid or invalid index value.
     */
    private static UiMessage markTask(String[] command, TaskList list) throws DukeException {
        try {
            if (command.length > 2) {
                throw wrongCommandFormat;
            }
            int index = Integer.parseInt(command[1]) - 1;
            list.markedTask(index);
            return new UiMessage(CommandMap.mark, list.getTask(index));
        } catch (NumberFormatException e) {
            throw noNumericParam;
        }

    }

    
    /** 
     * Unmark a task from the list.
     * @param command the command segments based on the user's input.
     * @param list the list of tasks.
     * @return UiMessage object containing what type of command to print out.
     * @throws DukeException if the command format is invalid or invalid index value.
     */
    private static UiMessage unmarkTask(String[] command, TaskList list) throws DukeException {
        try {
            if (command.length > 2) {
                throw wrongCommandFormat;
            }
            int index = Integer.parseInt(command[1]) - 1;
            list.unmarkedTask(index);
            return new UiMessage(CommandMap.unmark, list.getTask(index));
        } catch (NumberFormatException e) {
            throw noNumericParam;
        }

    }

    
    /** 
     * Deletes a task from the list based on the given index.
     * @param command the command segments based on the user's input.
     * @param list the list of tasks.
     * @return UiMessage object containing what type of command to print out.
     * @throws DukeException if the command format is invalid or invalid index value.
     */
    private static UiMessage deleteTask(String[] command, TaskList list) throws DukeException {
        try {
            if (command.length > 2) {
                throw wrongCommandFormat;
            }
            int index = Integer.parseInt(command[1]) - 1;
            Task deletedTask = list.getTask(index);
            list.deleteTask(index);
            return new UiMessage(CommandMap.delete, deletedTask);
        } catch (NumberFormatException e) {
            throw noNumericParam;
        }
    }

    
    /** 
     * Terminates the bot program.
     * 
     * @return UiMessage object containing what type of command to print out.
     */
    private static UiMessage closeProgram(String[] command) throws DukeException{
        if (command.length > 1) {
            throw wrongCommandFormat;
        }
        return new UiMessage(CommandMap.bye, null);
    }

    
    /** 
     * Creates a toDo object and adds it into the list.
     * @param command the command segments based on the user's input.
     * @param list the list of tasks.
     * @return UiMessage object containing what type of command to print out.
     * @throws DukeException if the command format is invalid or does not have any parameter.
     */
    private static UiMessage createToDo(String[] command, TaskList list) throws DukeException {
        if (command.length > 2) {
            throw wrongCommandFormat;
        }

        if (command.length == 1) {
            throw emptyParam;
        }

        Task toDoObj = new ToDo(command[1]);
        list.addTask(toDoObj);

        return new UiMessage(CommandMap.todo, toDoObj);
    }

    
    /** 
     * Creates a Deadline object and adds it into the list.
     * @param command the command segments based on the user's input
     * @param list the list of tasks.
     * @return UiMessage object containing what type of command to print out.
     * @throws DukeException if the command format is invalid or missing any special parameter (/by e.g)
     */
    private static UiMessage createDeadline(String[] command, TaskList list) throws DukeException {
        if (command.length != 4) {
            throw wrongCommandFormat;
        }

        if (!command[2].equals("/by")) {
            throw noSpecialParam;
        }

        Task deadlineObj = new Deadline(command[1], command[3]);
        list.addTask(deadlineObj);

        return new UiMessage(CommandMap.deadline, deadlineObj);
    }

    
    /** 
     * Creates an Event object and adds it into the list.
     * @param command the command segments based on the user's input
     * @param list the list of tasks.
     * @return UiMessage object containing what type of command to print out.
     * @throws DukeException if the command format is invalid or missing any special parameter (/by e.g)
     */
    private static UiMessage createEvent(String[] command, TaskList list) throws DukeException {
        if (command.length != 6) {
            throw wrongCommandFormat;
        }

        if (!command[2].equals("/from") || !command[4].equals("/to")) {
            throw noSpecialParam;
        }

        Task eventObj = new Event(command[1], command[3], command[5]);
        list.addTask(eventObj);

        return new UiMessage(CommandMap.event, eventObj);
    }

    
    /** 
     * Reads the input of the user and parse the input accordingly.
     * @param input user input
     * @param list list of tasks
     * @return UiMessage object containing what type of command to print out.
     * @throws DukeException if invalid command keyword is typed.
     */
    public static UiMessage readCommand(String input, TaskList list) throws DukeException {
        String[] command = input.trim().split(" ");

        try {
            switch (CommandMap.valueOf(command[0])) {
                case list:
                    return printTasks(command, list);
                case mark:
                    return markTask(command, list);
                case unmark:
                    return unmarkTask(command, list);
                case delete:
                    return deleteTask(command, list);
                case bye:
                    return closeProgram(command);
                case deadline:
                    return createDeadline(command, list);
                case event:
                    return createEvent(command, list);
                default:
                    return createToDo(command, list);
            }
        } catch (IllegalArgumentException e) {
            throw new DukeException("Invalid command.");
        }

    }

}
