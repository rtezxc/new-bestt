package com.astraclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class AstraClient implements ClientModInitializer {

    private static boolean guiOpen = false;
    private static boolean fullBright = false;
    private static boolean playerESP = false;
    private static boolean triggerBot = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            long window = client.getWindow().getHandle();

            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT)
                    == GLFW.GLFW_PRESS && !guiOpen) {
                client.setScreen(new AstraScreen());
                guiOpen = true;
            }

            if (client.currentScreen == null) {
                guiOpen = false;
            }

            if (fullBright) {
                client.options.getGamma().setValue(100.0);
            }
        });
    }

    private static class AstraScreen extends Screen {

        protected AstraScreen() {
            super(Text.literal("ASTRA CLIENT"));
        }

        @Override
        public void render(DrawContext context, int mouseX,
                           int mouseY, float delta) {

            int width = this.width;
            int height = this.height;

            context.fill(0, 0, width, height, 0xCC080B14);

            int panelW = 320;
            int panelH = 230;
            int x = (width - panelW) / 2;
            int y = (height - panelH) / 2;

            context.fill(x, y, x + panelW, y + panelH, 0xFF111827);
            context.fill(x, y, x + panelW, y + 3, 0xFF00E5FF);

            context.drawText(this.textRenderer,
                    "ASTRA CLIENT", x + 18, y + 16, 0xFF00E5FF, false);

            context.drawText(this.textRenderer,
                    "1.21.4  |  FABRIC", x + 18, y + 32,
                    0xFF9CA3AF, false);

            drawModule(context, x, y + 60, "FullBright", fullBright);
            drawModule(context, x, y + 91, "Player ESP", playerESP);
            drawModule(context, x, y + 122, "TriggerBot", triggerBot);

            context.drawText(this.textRenderer,
                    "CLICK MODULES TO TOGGLE",
                    x + 18, y + 190, 0xFF9CA3AF, false);

            context.drawText(this.textRenderer,
                    "RIGHT SHIFT: CLOSE",
                    x + 18, y + 207, 0xFF00E5FF, false);
        }

        private void drawModule(DrawContext context, int x, int y,
                                String name, boolean enabled) {
            context.fill(x + 14, y, x + 306, y + 25,
                    enabled ? 0xFF123C45 : 0xFF202938);

            context.drawText(this.textRenderer, name,
                    x + 24, y + 8, 0xFFFFFFFF, false);

            context.drawText(this.textRenderer,
                    enabled ? "ON" : "OFF",
                    x + 265, y + 8,
                    enabled ? 0xFF00E5FF : 0xFF9CA3AF, false);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY,
                                    int button) {
            int x = (this.width - 320) / 2;
            int y = (this.height - 230) / 2;

            if (mouseX >= x + 14 && mouseX <= x + 306) {
                if (mouseY >= y + 60 && mouseY <= y + 85)
                    fullBright = !fullBright;
                else if (mouseY >= y + 91 && mouseY <= y + 116)
                    playerESP = !playerESP;
                else if (mouseY >= y + 122 && mouseY <= y + 147)
                    triggerBot = !triggerBot;
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void close() {
            super.close();
            guiOpen = false;
        }
    }
}
