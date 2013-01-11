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
package lineage2.gameserver.skills.effects;

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.network.serverpackets.FlyToLocation;
import lineage2.gameserver.network.serverpackets.ValidateLocation;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.Location;

public class EffectTargetToMe extends Effect
{
	public EffectTargetToMe(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		Location flyLoc = _effected.getFlyLocation(getEffector(), getSkill());
		_effected.setLoc(flyLoc);
		_effected.broadcastPacket(new FlyToLocation(_effected, flyLoc, getSkill().getFlyType(), 0));
		_effected.broadcastPacket(new ValidateLocation(_effected));
	}
	
	@Override
	public void onExit()
	{
		super.onExit();
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}