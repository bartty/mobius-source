/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _329_CuriosityOfDwarf extends Quest implements ScriptFile
{
	private final int GOLEM_HEARTSTONE = 1346;
	private final int BROKEN_HEARTSTONE = 1365;
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	public _329_CuriosityOfDwarf()
	{
		super(false);
		addStartNpc(30437);
		addKillId(20083);
		addKillId(23026);
		addQuestItem(BROKEN_HEARTSTONE);
		addQuestItem(GOLEM_HEARTSTONE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("trader_rolento_q0329_03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("trader_rolento_q0329_06.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(SOUND_FINISH);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext;
		int id = st.getState();
		long heart;
		long broken;
		if (id == CREATED)
		{
			st.setCond(0);
		}
		if (st.getCond() == 0)
		{
			if ((st.getPlayer().getLevel() >= 33) && (st.getPlayer().getLevel() <= 38))
			{
				htmltext = "trader_rolento_q0329_02.htm";
			}
			else
			{
				htmltext = "trader_rolento_q0329_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else
		{
			heart = st.getQuestItemsCount(GOLEM_HEARTSTONE);
			broken = st.getQuestItemsCount(BROKEN_HEARTSTONE);
			if ((broken + heart) > 0)
			{
				st.giveItems(ADENA_ID, (50 * broken) + (1000 * heart));
				st.takeItems(BROKEN_HEARTSTONE, -1);
				st.takeItems(GOLEM_HEARTSTONE, -1);
				htmltext = "trader_rolento_q0329_05.htm";
			}
			else
			{
				htmltext = "trader_rolento_q0329_04.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int n = Rnd.get(1, 100);
		if (npcId == 23026)
		{
			if (n < 5)
			{
				st.giveItems(GOLEM_HEARTSTONE, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (n < 58)
			{
				st.giveItems(BROKEN_HEARTSTONE, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		else if (npcId == 20083)
		{
			if (n < 6)
			{
				st.giveItems(GOLEM_HEARTSTONE, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (n < 56)
			{
				st.giveItems(BROKEN_HEARTSTONE, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}