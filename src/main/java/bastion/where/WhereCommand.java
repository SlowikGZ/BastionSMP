package bastion.where;

import bastion.Bastion;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandSource.suggestMatching;

public class WhereCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("where").
                then(CommandManager.argument("player", StringArgumentType.word()).
                        suggests((c, b) -> suggestMatching(Bastion.getPlayers(c.getSource()), b)).
                        executes(context -> sendLocation(context.getSource(), StringArgumentType.getString(context, "player")))));
    }

    public static int sendLocation(ServerCommandSource source, String player) {
        ServerPlayerEntity playerEntity = source.getMinecraftServer().getPlayerManager().getPlayer(player);
        if (playerEntity != null){
            String playerPos = Bastion.formatCoords(playerEntity.getPos().x, playerEntity.getPos().y, playerEntity.getPos().z);
            String dimension = Bastion.getDimensionWithColor(playerEntity);
            source.sendFeedback(new LiteralText(Formatting.YELLOW + player + " " + dimension + " " + playerPos), false);
        }
        return 1;
    }
}
