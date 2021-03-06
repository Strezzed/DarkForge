/*******************************************************************************
 *     DarkForge a Forge Hacked Client
 *     Copyright (C) 2017  Hexeption (Keir Davis)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package uk.co.hexeption.darkforge.managers;

import uk.co.hexeption.darkforge.api.logger.LogHelper;
import uk.co.hexeption.darkforge.command.Command;
import uk.co.hexeption.darkforge.command.commands.Friend;
import uk.co.hexeption.darkforge.command.commands.TestCommand;
import uk.co.hexeption.darkforge.command.commands.Waypoint;
import uk.co.hexeption.darkforge.command.commands.Xray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private final Pattern patten = Pattern.compile("([^\"']\\S*|\".+?\"|'.+?')\\s*");

    private final List<Command> commands = new ArrayList<>();


    public void Initialization() {

        initCommands();
        LogHelper.info(String.format("Commands Loaded: %s!", commands.size()));
    }

    private void initCommands() {

        addCommand(new TestCommand(), new Xray(), new Friend(), new Waypoint());
    }

    public List<Command> getCommands() {

        return commands;
    }

    public boolean executeCommand(String message) {

        String commandName = message.contains(" ") ? message.split(" ")[0] : message;
        for (Command command : commands) {
            for (String alias : command.getName()) {
                if (alias.toLowerCase().equals(commandName.toLowerCase())) {
                    if (message.contains(" ")) {
                        if (message.split(" ")[1].equalsIgnoreCase("aliases")) {
                            listAllNames(command);
                            return true;
                        }
                    }
                    tryCommand(command, message);
                    return true;
                }
            }
        }
        return false;
    }


    public void addCommand(Command... command) {

        synchronized (this.commands) {
            this.commands.addAll(Arrays.asList(command));
        }
    }

    private void listAllNames(Command command) {

        String namesList = "Available names: ";
        String[] name = command.getName();
        for (int i = 0; i < name.length; i++) {
            String names = name[i];
            namesList += names + (i != name.length ? "," : "");
            System.out.println(namesList);
        }
    }

    public void tryCommand(Command command, String message) {

        try {

            String input = message.substring(1);
            String[] args;

            if (input.contains(" ")) {
                args = input.substring(input.indexOf(" ") + 1).split(" ");
            } else {
                args = new String[0];
            }

            command.execute(input, args);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
