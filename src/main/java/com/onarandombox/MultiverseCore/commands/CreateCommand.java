/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2020.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package com.onarandombox.MultiverseCore.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.commandTools.contexts.Flag;
import com.onarandombox.MultiverseCore.commandTools.contexts.WorldFlags;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@CommandAlias("mv")
public class CreateCommand extends MultiverseCommand {

    private static final Set<Flag<?>> FLAG_SET = new HashSet<>(Flag.all());

    public CreateCommand(MultiverseCore plugin) {
        super(plugin);
    }

    @Subcommand("create")
    @CommandPermission("multiverse.core.create")
    @Syntax("<name> <env> -s [seed] -g [generator[:id]] -t [worldtype] [-n] -a [true|false]")
    @CommandCompletion("@empty @environments @worldSettings:-s,-g,-t,-n,-a")
    @Description("Creates a new world and loads it.")
    public void onCreateCommand(@NotNull CommandSender sender,

                                @Syntax("<name>")
                                @Description("New world name.")
                                @NotNull @Flags("trim") @Conditions("creatableWorldName") String worldName,

                                @Syntax("<env>")
                                @Description("The world's environment. See: /mv env")
                                @NotNull World.Environment environment,

                                @Syntax("[world-flags]")
                                @Description("Other world settings. See: http://gg.gg/nn8bl")
                                @Nullable @Optional String[] flagsArray) {

        WorldFlags flags = new WorldFlags(this.plugin, sender, flagsArray, FLAG_SET);

        Command.broadcastCommandMessage(sender, String.format("Starting creation of world '%s'...", worldName));
        Command.broadcastCommandMessage(sender, (this.plugin.getMVWorldManager().addWorld(
                worldName,
                environment,
                // TODO API: Should Allow WorldFlags object to be passed directly
                flags.getValue(Flag.SEED),
                flags.getValue(Flag.WORLD_TYPE),
                flags.getValue(Flag.GENERATE_STRUCTURES),
                flags.getValue(Flag.GENERATOR),
                flags.getValue(Flag.SPAWN_ADJUST))
        )
                ? String.format("%sComplete!", ChatColor.GREEN)
                : String.format("%sFailed! See console for errors.", ChatColor.RED));
    }
}
