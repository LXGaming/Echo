/*
 * Copyright 2019 lolnet.co.nz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.lolnet.echo.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;

public class EchoCommand implements CommandExecutor {
    
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("This command can only be executed by players.", TextColors.RED));
            return CommandResult.empty();
        }
        
        Player player = (Player) src;
        Collection<String> arguments = args.getAll(Text.of("arguments"));
        if (arguments.isEmpty()) {
            player.sendMessage(Text.of("Invalid arguments", TextColors.RED));
            return CommandResult.empty();
        }
        
        Text message = Text.of(String.join(" ", arguments));
        try (CauseStackManager.StackFrame stackFrame = Sponge.getCauseStackManager().pushCauseFrame()) {
            stackFrame.pushCause(player);
            stackFrame.addContext(EventContextKeys.PLAYER_SIMULATED, player.getProfile());
            player.sendMessage(message);
            player.simulateChat(message, Sponge.getCauseStackManager().getCurrentCause());
            return CommandResult.success();
        }
    }
}