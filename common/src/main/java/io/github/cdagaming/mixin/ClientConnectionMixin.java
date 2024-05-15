package io.github.cdagaming.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ClientConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        final Minecraft mc = Minecraft.getInstance();
        if (packet instanceof ServerboundInteractPacket interactPacket) {
            interactPacket.dispatch(new ServerboundInteractPacket.Handler() {
                @Override
                public void onInteraction(InteractionHand interactionHand) {

                }

                @Override
                public void onInteraction(InteractionHand interactionHand, Vec3 vec3) {

                }

                @Override
                public void onAttack() {
                    HitResult hitResult = mc.hitResult;

                    if (hitResult == null) return;
                    if (hitResult.getType() != HitResult.Type.ENTITY) return;
                    Entity entity = ((EntityHitResult) hitResult).getEntity();
                    if (!(entity instanceof EndCrystal)) return;

                    assert mc.player != null;
                    MobEffectInstance weakness = mc.player.getEffect(MobEffects.WEAKNESS);
                    MobEffectInstance strength = mc.player.getEffect(MobEffects.DAMAGE_BOOST);
                    if (weakness == null || strength == null) return;
                    boolean hasTool = ClientConnectionMixin.this.isTool(mc.player.getMainHandItem());
                    if (strength.getAmplifier() <= weakness.getAmplifier() && !hasTool) return;
                    entity.kill();
                    entity.setRemoved(Entity.RemovalReason.KILLED);
                    entity.onClientRemoval();
                    System.out.println("End crystal removed");
                }
            });
        }
    }

    private boolean isTool(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (!(item instanceof TieredItem)) return false;
        if (item instanceof HoeItem) return false;
        Tier material = ((TieredItem) item).getTier();
        return material == Tiers.DIAMOND || material == Tiers.NETHERITE;
    }
}