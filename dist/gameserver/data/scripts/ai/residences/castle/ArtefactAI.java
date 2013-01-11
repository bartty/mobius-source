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
package ai.residences.castle;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.instances.NpcInstance;

public class ArtefactAI extends CharacterAI
{
	public ArtefactAI(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
		NpcInstance actor;
		Player player;
		if ((attacker == null) || ((player = attacker.getPlayer()) == null) || ((actor = (NpcInstance) getActor()) == null))
		{
			return;
		}
		SiegeEvent<?, ?> siegeEvent1 = actor.getEvent(SiegeEvent.class);
		SiegeEvent<?, ?> siegeEvent2 = player.getEvent(SiegeEvent.class);
		SiegeClanObject siegeClan = siegeEvent1.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
		if ((siegeEvent2 == null) || ((siegeEvent1 == siegeEvent2) && (siegeClan != null)))
		{
			ThreadPoolManager.getInstance().schedule(new notifyGuard(player), 1000);
		}
	}
	
	class notifyGuard extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		public notifyGuard(Player attacker)
		{
			_playerRef = attacker.getRef();
		}
		
		@Override
		public void runImpl()
		{
			NpcInstance actor;
			Player attacker = _playerRef.get();
			if ((attacker == null) || ((actor = (NpcInstance) getActor()) == null))
			{
				return;
			}
			for (NpcInstance npc : actor.getAroundNpc(1500, 200))
			{
				if (npc.isSiegeGuard() && Rnd.chance(20))
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
				}
			}
			if ((attacker.getCastingSkill() != null) && (attacker.getCastingSkill().getTargetType() == Skill.SkillTargetType.TARGET_HOLY))
			{
				ThreadPoolManager.getInstance().schedule(this, 10000);
			}
		}
	}
}