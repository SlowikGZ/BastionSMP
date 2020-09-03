package bastion.mixins;

import bastion.Bastion;
import bastion.discord.DiscordListener;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class BastionOnPlayerDeathMixin extends PlayerEntity {
    public BastionOnPlayerDeathMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/CombatEventS2CPacket;<init>(Lnet/minecraft/entity/damage/DamageTracker;Lnet/minecraft/network/packet/s2c/play/CombatEventS2CPacket$Type;Lnet/minecraft/text/Text;)V", ordinal = 0))
    public void onPlayerDies(DamageSource source, CallbackInfo ci) {
        if (DiscordListener.chatBridge){
            DiscordListener.sendMessage(":skull_crossbones: **" + this.getDamageTracker().getDeathMessage().getString().replace("_", "\\_") + "**");
        }
        this.sendMessage(new LiteralText("RIP ;( : " + Bastion.getDimensionWithColor(this.world) + Bastion.formatCoords(this.getPos().x, this.getPos().y, this.getPos().z)), false);
    }
}
