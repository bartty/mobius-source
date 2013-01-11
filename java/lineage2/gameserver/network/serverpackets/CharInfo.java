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
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.Config;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.instances.DecoyInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.skills.effects.EffectCubic;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharInfo extends L2GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(CharInfo.class);
	private int[][] _inv;
	private int _mAtkSpd, _pAtkSpd, mevasion, maccuracy, mCritRate;
	private int _swimWalkSpd, _swimRunSpd, _runSpd, _walkSpd, _swimSpd, _flRunSpd, _flWalkSpd, _flyRunSpd, _flyWalkSpd;
	private Location _loc, _fishLoc;
	private String _name, _title;
	private int _objId, _race, _sex, base_class, pvp_flag, karma, rec_have;
	private double speed_move, speed_atack, col_radius, col_height;
	private int hair_style, hair_color, face, _abnormalEffect, _abnormalEffect2;
	private int clan_id, clan_crest_id, large_clan_crest_id, ally_id, ally_crest_id, class_id;
	private int _sit, _run, _combat, _dead, private_store, _enchant;
	private int _noble, _hero, _fishing, mount_type;
	private int plg_class, pledge_type, clan_rep_score, cw_level, mount_id;
	private int _nameColor, _title_color, _transform, _agathion, _clanBoatObjectId;
	private EffectCubic[] cubics;
	private boolean _isPartyRoomLeader, _isFlying;
	private TeamType _team;
	@SuppressWarnings("unused")
	private int curCP, curHP, maxHP, curMP, maxMP;
	
	public CharInfo(Player cha)
	{
		this((Creature) cha);
	}
	
	public CharInfo(DecoyInstance cha)
	{
		this((Creature) cha);
	}
	
	public CharInfo(Creature cha)
	{
		if (cha == null)
		{
			System.out.println("CharInfo: cha is null!");
			Thread.dumpStack();
			return;
		}
		if (cha.isInvisible())
		{
			return;
		}
		if (cha.isDeleted())
		{
			return;
		}
		Player player = cha.getPlayer();
		if (player == null)
		{
			return;
		}
		if (player.isInBoat())
		{
			_loc = player.getInBoatPosition();
			if (player.isClanAirShipDriver())
			{
				_clanBoatObjectId = player.getBoat().getObjectId();
			}
		}
		if (_loc == null)
		{
			_loc = cha.getLoc();
		}
		_objId = cha.getObjectId();
		if ((player.getTransformationName() != null) || (((player.getReflection() == ReflectionManager.GIRAN_HARBOR) || (player.getReflection() == ReflectionManager.PARNASSUS)) && (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)))
		{
			_name = player.getTransformationName() != null ? player.getTransformationName() : player.getName();
			_title = "";
			clan_id = 0;
			clan_crest_id = 0;
			ally_id = 0;
			ally_crest_id = 0;
			large_clan_crest_id = 0;
			if (player.isCursedWeaponEquipped())
			{
				cw_level = CursedWeaponsManager.getInstance().getLevel(player.getCursedWeaponEquippedId());
			}
		}
		else
		{
			_name = player.getName();
			if (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
			{
				_title = "";
			}
			else if (!player.isConnected())
			{
				_title = "NO CARRIER";
				_title_color = 255;
			}
			else
			{
				_title = player.getTitle();
				_title_color = player.getTitleColor();
			}
			Clan clan = player.getClan();
			Alliance alliance = clan == null ? null : clan.getAlliance();
			clan_id = clan == null ? 0 : clan.getClanId();
			clan_crest_id = clan == null ? 0 : clan.getCrestId();
			large_clan_crest_id = clan == null ? 0 : clan.getCrestLargeId();
			ally_id = alliance == null ? 0 : alliance.getAllyId();
			ally_crest_id = alliance == null ? 0 : alliance.getAllyCrestId();
			cw_level = 0;
		}
		if (player.isMounted())
		{
			_enchant = 0;
			mount_id = player.getMountNpcId() + 1000000;
			mount_type = player.getMountType();
		}
		else
		{
			_enchant = player.getEnchantEffect();
			mount_id = 0;
			mount_type = 0;
		}
		_inv = new int[Inventory.PAPERDOLL_MAX][2];
		for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
		{
			_inv[PAPERDOLL_ID][0] = player.getInventory().getPaperdollItemId(PAPERDOLL_ID);
			_inv[PAPERDOLL_ID][1] = player.getInventory().getPaperdollAugmentationId(PAPERDOLL_ID);
		}
		_mAtkSpd = player.getMAtkSpd();
		_pAtkSpd = player.getPAtkSpd();
		speed_move = player.getMovementSpeedMultiplier();
		_runSpd = (int) (player.getRunSpeed() / speed_move);
		_walkSpd = (int) (player.getWalkSpeed() / speed_move);
		_flRunSpd = 0;
		_flWalkSpd = 0;
		if (player.isFlying())
		{
			_flyRunSpd = _runSpd;
			_flyWalkSpd = _walkSpd;
		}
		else
		{
			_flyRunSpd = 0;
			_flyWalkSpd = 0;
		}
		_swimSpd = player.getSwimSpeed();
		_race = player.getRace().ordinal();
		_swimRunSpd = player.getSwimRunSpeed();
		_swimWalkSpd = player.getSwimWalkSpeed();
		_sex = player.getSex();
		base_class = player.getBaseClassId();
		pvp_flag = player.getPvpFlag();
		karma = player.getKarma();
		speed_atack = player.getAttackSpeedMultiplier();
		col_radius = player.getColRadius();
		col_height = player.getColHeight();
		hair_style = player.getHairStyle();
		hair_color = player.getHairColor();
		face = player.getFace();
		if ((clan_id > 0) && (player.getClan() != null))
		{
			clan_rep_score = player.getClan().getReputationScore();
		}
		else
		{
			clan_rep_score = 0;
		}
		_sit = player.isSitting() ? 0 : 1;
		_run = player.isRunning() ? 1 : 0;
		_combat = player.isInCombat() ? 1 : 0;
		_dead = player.isAlikeDead() ? 1 : 0;
		private_store = player.isInObserverMode() ? Player.STORE_OBSERVING_GAMES : player.getPrivateStoreType();
		cubics = player.getCubics().toArray(new EffectCubic[player.getCubics().size()]);
		_abnormalEffect = player.getAbnormalEffect();
		_abnormalEffect2 = player.getAbnormalEffect2();
		rec_have = player.isGM() ? 0 : player.getRecomHave();
		class_id = player.getClassId().getId();
		_team = player.getTeam();
		_noble = player.isNoble() ? 1 : 0;
		_hero = player.isHero() || (player.isGM() && Config.GM_HERO_AURA) ? 1 : 0;
		_fishing = player.isFishing() ? 1 : 0;
		_fishLoc = player.getFishLoc();
		_nameColor = player.getNameColor();
		plg_class = player.getPledgeClass();
		pledge_type = player.getPledgeType();
		_transform = player.getTransformation();
		_agathion = player.getAgathionId();
		_isPartyRoomLeader = (player.getMatchingRoom() != null) && (player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING) && (player.getMatchingRoom().getLeader() == player);
		_isFlying = player.isInFlyingTransform();
		mevasion = player.getMEvasionRate(null);
		maccuracy = player.getMAccuracy();
		mCritRate = (int) player.getMagicCriticalRate(null, null);
		curCP = (int) player.getCurrentCp();
		curHP = (int) player.getCurrentHp();
		maxHP = player.getMaxHp();
		curMP = (int) player.getCurrentMp();
		maxMP = player.getMaxMp();
	}
	
	@Override
	protected final void writeImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (_objId == 0)
		{
			return;
		}
		if (activeChar.getObjectId() == _objId)
		{
			_log.error("You cant send CharInfo about his character to active user!!!");
			return;
		}
		if (!activeChar.isTautiClient())
		{
			writeC(0x31);
			writeD(_loc.x);
			writeD(_loc.y);
			writeD(_loc.z + Config.CLIENT_Z_SHIFT);
			writeD(_clanBoatObjectId);
			writeD(_objId);
			writeS(_name);
			writeD(_race);
			writeD(_sex);
			writeD(base_class);
			for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
			{
				writeD(_inv[PAPERDOLL_ID][0]);
			}
			for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
			{
				writeD(_inv[PAPERDOLL_ID][1]);
			}
			writeD(0x01);
			writeD(0x00);
			writeD(pvp_flag);
			writeD(karma);
			writeD(mevasion);
			writeD(maccuracy);
			writeD(mCritRate);
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			writeD(0x00);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_swimSpd);
			writeD(_swimSpd);
			writeD(_flRunSpd);
			writeD(_flWalkSpd);
			writeD(_flyRunSpd);
			writeD(_flyWalkSpd);
			writeF(speed_move);
			writeF(speed_atack);
			writeF(col_radius);
			writeF(col_height);
			writeD(hair_style);
			writeD(hair_color);
			writeD(face);
			writeS(_title);
			writeD(clan_id);
			writeD(clan_crest_id);
			writeD(ally_id);
			writeD(ally_crest_id);
			writeC(_sit);
			writeC(_run);
			writeC(_combat);
			writeC(_dead);
			writeC(0x00);
			writeC(mount_type);
			writeC(private_store);
			writeH(cubics.length);
			for (EffectCubic cubic : cubics)
			{
				writeH(cubic == null ? 0 : cubic.getId());
			}
			writeC(_isPartyRoomLeader ? 0x01 : 0x00);
			writeD(_abnormalEffect);
			writeC(_isFlying ? 0x02 : 0x00);
			writeH(rec_have);
			writeD(mount_id);
			writeD(class_id);
			writeD(0x00);
			writeC(_enchant);
			writeC(_team.ordinal());
			writeD(large_clan_crest_id);
			writeC(_noble);
			writeC(_hero);
			writeC(_fishing);
			writeD(_fishLoc.x);
			writeD(_fishLoc.y);
			writeD(_fishLoc.z);
			writeD(_nameColor);
			writeD(_loc.h);
			writeD(plg_class);
			writeD(pledge_type);
			writeD(_title_color);
			writeD(cw_level);
			writeD(clan_rep_score);
			writeD(_transform);
			writeD(_agathion);
			writeD(0x01);
			writeD(_abnormalEffect2);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(curHP);
			writeD(maxHP);
			writeD(curMP);
			writeD(maxMP);
			writeD(0x00);
			writeC(0x00);
		}
		else
		{
			writeC(0x31);
			writeD(_loc.x);
			writeD(_loc.y);
			writeD(_loc.z + Config.CLIENT_Z_SHIFT);
			writeD(_clanBoatObjectId);
			writeD(_objId);
			writeS(_name);
			writeD(_race);
			writeD(_sex);
			writeD(base_class);
			for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
			{
				writeD(_inv[PAPERDOLL_ID][0]);
			}
			for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
			{
				writeH(_inv[PAPERDOLL_ID][1]);
				writeH(0x00);
			}
			writeD(0x00);
			writeD(0x00);
			writeD(pvp_flag);
			writeD(karma);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			writeD(0x00);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_swimRunSpd);
			writeD(_swimWalkSpd);
			writeD(_flRunSpd);
			writeD(_flWalkSpd);
			writeD(_flyRunSpd);
			writeD(_flyWalkSpd);
			writeF(speed_move);
			writeF(speed_atack);
			writeF(col_radius);
			writeF(col_height);
			writeD(hair_style);
			writeD(hair_color);
			writeD(face);
			writeS(_title);
			writeD(clan_id);
			writeD(clan_crest_id);
			writeD(ally_id);
			writeD(ally_crest_id);
			writeC(_sit);
			writeC(_run);
			writeC(_combat);
			writeC(_dead);
			writeC(0x00);
			writeC(mount_type);
			writeC(private_store);
			writeH(cubics.length);
			for (EffectCubic cubic : cubics)
			{
				writeH(cubic == null ? 0 : cubic.getId());
			}
			writeC(_isPartyRoomLeader ? 0x01 : 0x00);
			writeC(_isFlying ? 0x02 : 0x00);
			writeH(rec_have);
			writeD(mount_id);
			writeD(class_id);
			writeD(0x00);
			writeC(_enchant);
			writeC(_team.ordinal());
			writeD(large_clan_crest_id);
			writeC(_noble);
			writeC(_hero);
			writeC(_fishing);
			writeD(_fishLoc.x);
			writeD(_fishLoc.y);
			writeD(_fishLoc.z);
			writeD(_nameColor);
			writeD(_loc.h);
			writeD(plg_class);
			writeD(pledge_type);
			writeD(_title_color);
			writeD(cw_level);
			writeD(clan_rep_score);
			writeD(_transform);
			writeD(_agathion);
			writeD(0x01);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeC(0x00);
			writeD(0x00);
			writeC(0x00);
		}
	}
	
	public static final int[] PAPERDOLL_ORDER =
	{
		Inventory.PAPERDOLL_UNDER,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_BACK,
		Inventory.PAPERDOLL_LRHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_DHAIR,
		Inventory.PAPERDOLL_RBRACELET,
		Inventory.PAPERDOLL_LBRACELET,
		Inventory.PAPERDOLL_DECO1,
		Inventory.PAPERDOLL_DECO2,
		Inventory.PAPERDOLL_DECO3,
		Inventory.PAPERDOLL_DECO4,
		Inventory.PAPERDOLL_DECO5,
		Inventory.PAPERDOLL_DECO6,
		Inventory.PAPERDOLL_BELT
	};
}