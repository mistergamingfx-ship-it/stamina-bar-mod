package com.example.staminabar.client;

import com.example.staminabar.StaminaBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientStaminaHandler {
    private static double stamina = 100.0D;
    private static int regenDelay = 0;

    private static double maxStamina() {
        return Math.max(20, StaminaBar.Settings.maximumStamina);
    }

    @SubscribeEvent
    public static void onClientTick(net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent event) {
        if (event.phase != net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null || mc.world == null) {
            stamina = maxStamina();
            regenDelay = 0;
            return;
        }

        if (player.isSprinting()) {
            stamina -= StaminaBar.Settings.sprintDrain;
            regenDelay = 12;
            if (stamina <= 0.0D) {
                stamina = 0.0D;
                player.setSprinting(false);
            }
        } else {
            if (regenDelay > 0) {
                regenDelay--;
            } else {
                stamina += StaminaBar.Settings.recovery;
            }
        }

        stamina = Math.max(0.0D, Math.min(maxStamina(), stamina));
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerSP)) return;

        stamina = Math.max(0.0D, stamina - StaminaBar.Settings.jumpCost);
        if (stamina <= 0.0D) {
            ((EntityPlayerSP) event.getEntityLiving()).setSprinting(false);
        }
        regenDelay = 8;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!StaminaBar.Settings.showBar) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        ScaledResolution resolution = event.getResolution();
        int width = 142;
        int height = 9;
        int x = resolution.getScaledWidth() / 2 - width / 2;
        int y = resolution.getScaledHeight() - 49;

        double ratio = stamina / maxStamina();
        int fill = (int) Math.round((width - 2) * ratio);
        int color = ratio > 0.55D ? 0xFF45D65A : (ratio > 0.25D ? 0xFFFFC533 : 0xFFE34A4A);

        Gui.drawRect(x, y, x + width, y + height, 0xB0000000);
        Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF333333);
        if (fill > 0) {
            Gui.drawRect(x + 1, y + 1, x + 1 + fill, y + height - 1, color);
        }

        String label = TextFormatting.YELLOW + "STAMINA";
        mc.fontRenderer.drawStringWithShadow(label, x, y - 11, 0xFFFFFF);
        String value = ((int) Math.ceil(stamina)) + "/" + (int) maxStamina();
        mc.fontRenderer.drawStringWithShadow(value, x + width - mc.fontRenderer.getStringWidth(value), y - 11, 0xFFFFFF);
    }
}
