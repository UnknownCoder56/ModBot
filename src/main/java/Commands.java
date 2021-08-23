import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Commands {

    public static Map<String, Warn> warnMap = new HashMap<>();

    public static void help(MessageReceivedEvent event) {
        sendMessage(event, """
                Commands:-
                1) -kick (mention)
                2) -ban (mention) (days)
                3) -warn (mention),(cause)
                4) -clearwarn (mention)
                5) -warns (mention)
                6) -ping
                7) -info""");
    }

    public static void kick(MessageReceivedEvent event) {
        try {
            Member member = event.getMessage().getMentionedMembers().get(event.getMessage().getMentionedMembers().size() - 1);
            member.kick().queue();
            sendMessage(event, "Successfully kicked " + member.getEffectiveName() + "!");
        } catch (HierarchyException ex) {
            sendMessage(event, "That member has a higher or equal role or position than me! Can't kick.");
        } catch (InsufficientPermissionException ex) {
            sendMessage(event, "I don't have admin perms! Give me a role with admin perms.");
        } catch (IndexOutOfBoundsException ex) {
            sendMessage(event, "Either no one was mentioned, or the mentioned user is a bot / not of this server.");
        }
    }

    public static void ban(MessageReceivedEvent event) {
        try {
            String[] args = event.getMessage().getContentRaw().split(" ");
            Member member = event.getMessage().getMentionedMembers().get(0);
            member.ban(Integer.parseInt(args[2])).queue();
            sendMessage(event, "Successfully banned " + member.getEffectiveName() + " for " + args[2] + " days!");
        } catch (ArrayIndexOutOfBoundsException ex) {
            sendMessage(event, "All arguments were not given! Syntax -> -ban (mention) (days number)");
        } catch (HierarchyException ex) {
            sendMessage(event, "That member has a higher or equal role or position than me! Can't ban.");
        } catch (InsufficientPermissionException ex) {
            sendMessage(event, "I don't have admin perms! Give me a role with admin perms.");
        } catch (NumberFormatException ex) {
            sendMessage(event, "(DAYS) argument was incorrect. Should be an integer. Syntax -> -ban (mention) (days number)");
        }
    }

    public static void warn(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(",");
        Member member = event.getMessage().getMentionedMembers().get(0);
        if (!warnMap.containsKey(member.getEffectiveName())) {
            Warn warn = new Warn(args[1], member);
            warnMap.put(warn.toString(), warn);
        } else {
            warnMap.get(member.getEffectiveName()).newWarn(args[1]);
        }
        sendMessage(event, "Successfully warned " + member.getEffectiveName() + " for cause:\n" +
                args[1] + "\nHe/She now has " + warnMap.get(member.getEffectiveName()).warns + " warn(s).");
        User user = member.getUser();
        PrivateChannel channel = user.openPrivateChannel().complete();
        channel.sendMessage("You have been warned in " + event.getGuild().getName() + " for reason: " + args[1]).queue();
        channel.sendMessage("You now have " + warnMap.get(member.getEffectiveName()).warns + " warn(s) in that server.").queue();
    }

    public static void warns(MessageReceivedEvent event) {
        Member member = event.getMessage().getMentionedMembers().get(0);
        sendMessage(event, "Warns for " + member.getEffectiveName() + ":-");
        if (warnMap.containsKey(member.getEffectiveName())) {
            Warn warn = warnMap.get(member.getEffectiveName());
            for (String warnCause : warn.warnCauses) {
                sendMessage(event, warnCause);
            }
        } else {
            sendMessage(event, "No warns were found for this user!");
        }
    }

    public static void clearWarn(MessageReceivedEvent event) {
        Member member = event.getMessage().getMentionedMembers().get(0);
        warnMap.remove(member.getEffectiveName());
        sendMessage(event, "Successfully removed all warnings for " + member.getEffectiveName() + "!");
    }

    public static void ping(MessageReceivedEvent event) {
        Main.jda.getRestPing().queue( (time)  ->
                event.getChannel().sendMessageFormat("Ping: %d ms", time).queue());
    }

    public static void info(MessageReceivedEvent event) {
        StringBuilder guilds = new StringBuilder();
        int totalMembers = 0;
        for (Guild guild : Main.jda.getGuilds()) {
            guilds.append(guild.getName()).append("\n");
            totalMembers += guild.getMemberCount();
        }
        StringBuilder roles = new StringBuilder();
        for (Role role : Main.jda.getRoles()) {
            roles.append(role.getName()).append("\n");
        }
        Permission[] permissions = {Permission.ADMINISTRATOR};
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("Details for ModBot:-")
                        .setAuthor("ModBot")
                        .setTimestamp(LocalDateTime.now())
                        .setColor(Color.RED)
                        .addField(new MessageEmbed.Field("Name", "ModBot", true))
                        .addField(new MessageEmbed.Field("Guilds joined", guilds.toString(), true))
                        .addField(new MessageEmbed.Field("Members using bot", Integer.toString(totalMembers), true))
                        .addField(new MessageEmbed.Field("Roles", roles.toString(), true))
                        .addField(new MessageEmbed.Field("Invite Link", Main.jda.getInviteUrl(permissions), true))
                .build()).queue();
    }

    public static void sendMessage(MessageReceivedEvent event, String message) {
        event.getChannel().sendMessage(message).queue();
    }
}
