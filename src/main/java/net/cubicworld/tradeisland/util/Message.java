package net.cubicworld.tradeisland.util;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.Locale;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class Message {
    public static final TextColor WARNING_COLOR = NamedTextColor.GOLD;
    public static final TextColor SUCCESS_COLOR = NamedTextColor.GREEN;
    public static final TextColor INFO_COLOR = NamedTextColor.WHITE;
    public static final TextColor ERROR_COLOR = NamedTextColor.RED;

    @Setter
    private static ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());

    public static TextComponent info(String key) {
        return Component.text(bundle.getString(key)).color(INFO_COLOR);
    }

    public static TextComponent success(String key) {
        return Component.text(bundle.getString(key)).color(SUCCESS_COLOR);
    }

    public static TextComponent warning(String key) {
        return Component.text(bundle.getString(key)).color(WARNING_COLOR);
    }

    public static TextComponent error(String key) {
        return Component.text(bundle.getString(key)).color(ERROR_COLOR);
    }
}
