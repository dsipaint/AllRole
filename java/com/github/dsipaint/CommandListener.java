package com.github.dsipaint;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		//admin-only command
		if(e.getMember().hasPermission(Permission.ADMINISTRATOR))
		{
			String msg = e.getMessage().getContentRaw();
			String[] args = msg.split(" ");
			
			//^allrole
			if(args[0].equalsIgnoreCase(Main.PREFIX + "allrole"))
			{
				//if no command parameter is given
				if(args.length == 1)
				{
					e.getChannel().sendMessage("Missing command parameter (give/take).").queue();
					return;
				}
				
				if(args[1].equalsIgnoreCase("give"))
				{
					//no role specified
					if(args.length == 2)
					{
						e.getChannel().sendMessage("No role specified.").queue();
						return;
					}
					
					String roleid;
					
					String fullarg = "";
					for(int i = 2; i < args.length; i++)
						fullarg += " " + args[i];
					
					fullarg = fullarg.substring(1);
					
					if(fullarg.matches("\\d{18}"))
					{
						if(e.getGuild().getRoleById(fullarg) != null)
							roleid = fullarg;
						else
						{
							e.getChannel().sendMessage("Error: this role does not exist").queue();
							return;
						}
					}
					else
					{
						List<Role> roles = e.getGuild().getRolesByName(fullarg, true);
						if(roles.isEmpty())
						{
							e.getChannel().sendMessage("No role was found with the name "
									+ fullarg + "!").queue();
							return;
						}
						//if a match was found
						roleid = roles.get(0).getId();
					}
					
					//we then have a role id that we can use
					Role role = e.getGuild().getRoleById(roleid);
					//give role to everyone
					for(Member m : e.getGuild().getMembers())
						e.getGuild().addRoleToMember(m, role).queue();
					
					e.getChannel().sendMessage("Successfully gave role " + role.getName()
							+ " to everyone on the server!").queue();
					
					return;
				}
				
				if(args[1].equalsIgnoreCase("take"))
				{
					//no role specified
					if(args.length == 2)
					{
						e.getChannel().sendMessage("No role specified.").queue();
						return;
					}
					
					String roleid;
					
					String fullarg = "";
					for(int i = 2; i < args.length; i++)
						fullarg += " " + args[i];
					
					fullarg = fullarg.substring(1);
					
					if(fullarg.matches("\\d{18}"))
					{
						if(e.getGuild().getRoleById(fullarg) != null)
							roleid = fullarg;
						else
						{
							e.getChannel().sendMessage("Error: this role does not exist").queue();
							return;
						}
					}
					else
					{
						List<Role> roles = e.getGuild().getRolesByName(fullarg, true);
						if(roles.isEmpty())
						{
							e.getChannel().sendMessage("No role was found with the name "
									+ fullarg + "!").queue();
							return;
						}
						//if a match was found
						roleid = roles.get(0).getId();
					}
					
					//we then have a role id that we can use
					Role role = e.getGuild().getRoleById(roleid);
					//remove role from everyone
					for(Member m : e.getGuild().getMembers())
						e.getGuild().removeRoleFromMember(m, role).queue();
					
					e.getChannel().sendMessage("Successfully removed role " + role.getName()
					+ " to everyone on the server!").queue();
					
					return;
				}
				
				//wrong parameter given (not give/take)
				e.getChannel().sendMessage("Incorrect syntax: use " + Main.PREFIX + "{give/take}"
						+ " {roleid}").queue();
				return;
			}
		}
	}
}
