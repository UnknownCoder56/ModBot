import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        String text = event.getMessage().getContentRaw();
        if (!event.getAuthor().isBot()) {
            if (text.contains("-help")) {
                Commands.help(event);
            } else if (text.contains("-kick ")) {
                Commands.kick(event);
            } else if (text.contains("-ban ")) {
                Commands.ban(event);
            } else if (text.contains("-warn ")) {
                Commands.warn(event);
            } else if (text.contains("-warns ")) {
                Commands.warns(event);
            } else if (text.contains("-clearwarn ")) {
                Commands.clearWarn(event);
            } else if (text.contains("-ping")) {
                Commands.ping(event);
            } else if (text.contains("-info")) {
                Commands.info(event);
            }
        }
    }
}
