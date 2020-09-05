package bastion.mixins;

import bastion.Bastion;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.SeedCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class BastionCommandManagerMixin {
    @Shadow @Final private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void registerCommands(CommandManager.RegistrationEnvironment environment, CallbackInfo ci){
        Bastion.registerCommands(this.dispatcher);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/SeedCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Z)V"))
    public void onRegister(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated){
        SeedCommand.register(dispatcher, false);
    }
}
