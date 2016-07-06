package com.andrewyunt.pyxiscore;

import com.palmergames.bukkit.towny.TownyMessaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;

public class BankConversation implements ConversationAbandonedListener {

    private final ConversationFactory conversationFactory;

    private int prompt;

    String action, prefix = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Pyx" + ChatColor.GOLD + "Bank" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    int amount;

    public BankConversation() {

        prompt = 1;

        conversationFactory = new ConversationFactory(PyxisCore.getInstance())
                .withModality(true)
                .withFirstPrompt(new CommandPrompt())
                .withEscapeSequence("quit")
                .withTimeout(30)
                .thatExcludesNonPlayersWithMessage("Unable to access from the console.")
                .addConversationAbandonedListener(this);
    }

    public void beginConversation(CommandSender sender) {

        conversationFactory.buildConversation((Conversable) sender).begin();
    }

    private class CommandPrompt extends StringPrompt {

        public String getPromptText(ConversationContext context) {

            if(prompt == 1)
                return prefix + ChatColor.GRAY + "What action would you like to perform? (Possible choices: deposit/withdraw)";
            else
                return prefix + ChatColor.GRAY + "How much PYX would you like to transfer to/from your account?";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {

            if(prompt == 1) {
                if(input.equalsIgnoreCase("deposit"))
                    action = "deposit";
                else if(input.equalsIgnoreCase("withdraw") || input.equalsIgnoreCase("withdrawal"))
                    action = "withdraw";

                prompt++;

                return new CommandPrompt();
            } else {
                try {
                    amount = Integer.parseInt(input);
                } catch(NumberFormatException e) {
                    return new CommandPrompt();
                }

                return Prompt.END_OF_CONVERSATION;
            }
        }
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent event) {

        Conversable who = event.getContext().getForWhom();

        if(!event.gracefulExit())
            TownyMessaging.sendErrorMsg((CommandSender) who, "Command execution stopped by " + event.getCanceller().getClass().getName());
        else
            Bukkit.getServer().dispatchCommand((CommandSender) who, "bank " + action + " " + amount);
    }
}