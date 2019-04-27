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

package nz.co.lolnet.echo;

import com.google.inject.Inject;
import nz.co.lolnet.echo.command.EchoCommand;
import nz.co.lolnet.echo.listener.EchoListener;
import nz.co.lolnet.echo.util.Reference;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(
        id = Reference.ID,
        name = Reference.NAME,
        version = Reference.VERSION,
        description = Reference.DESCRIPTION,
        authors = {Reference.AUTHORS},
        url = Reference.WEBSITE
)
public class Echo {
    
    private static Echo instance;
    
    @Inject
    private PluginContainer pluginContainer;
    
    @Inject
    private Logger logger;
    
    @Listener
    public void onConstruction(GameConstructionEvent event) {
        instance = this;
    }
    
    @Listener
    public void onInitialization(GameInitializationEvent event) {
        Sponge.getCommandManager().register(getPluginContainer(), CommandSpec.builder()
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("arguments")))
                .executor(new EchoCommand())
                .permission("echo.base")
                .build(), Reference.ID);
        
        Sponge.getEventManager().registerListeners(getPluginContainer(), new EchoListener());
    }
    
    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        getLogger().info("{} v{} has loaded", Reference.NAME, Reference.VERSION);
    }
    
    public static Echo getInstance() {
        return instance;
    }
    
    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }
    
    public Logger getLogger() {
        return logger;
    }
}