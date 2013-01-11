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

public class ExGoodsInventoryResult extends L2GameServerPacket
{
	public static L2GameServerPacket NOTHING = new ExGoodsInventoryResult(1);
	public static L2GameServerPacket SUCCESS = new ExGoodsInventoryResult(2);
	public static L2GameServerPacket ERROR = new ExGoodsInventoryResult(-1);
	public static L2GameServerPacket TRY_AGAIN_LATER = new ExGoodsInventoryResult(-2);
	public static L2GameServerPacket INVENTORY_FULL = new ExGoodsInventoryResult(-3);
	public static L2GameServerPacket NOT_CONNECT_TO_PRODUCT_SERVER = new ExGoodsInventoryResult(-4);
	public static L2GameServerPacket CANT_USE_AT_TRADE_OR_PRIVATE_SHOP = new ExGoodsInventoryResult(-5);
	public static L2GameServerPacket NOT_EXISTS = new ExGoodsInventoryResult(-6);
	public static L2GameServerPacket TO_MANY_USERS_TRY_AGAIN_INVENTORY = new ExGoodsInventoryResult(-101);
	public static L2GameServerPacket TO_MANY_USERS_TRY_AGAIN = new ExGoodsInventoryResult(-102);
	public static L2GameServerPacket PREVIOS_REQUEST_IS_NOT_COMPLETE = new ExGoodsInventoryResult(-103);
	public static L2GameServerPacket NOTHING2 = new ExGoodsInventoryResult(-104);
	public static L2GameServerPacket ALREADY_RETRACTED = new ExGoodsInventoryResult(-105);
	public static L2GameServerPacket ALREADY_RECIVED = new ExGoodsInventoryResult(-106);
	public static L2GameServerPacket PRODUCT_CANNOT_BE_RECEIVED_AT_CURRENT_SERVER = new ExGoodsInventoryResult(-107);
	public static L2GameServerPacket PRODUCT_CANNOT_BE_RECEIVED_AT_CURRENT_PLAYER = new ExGoodsInventoryResult(-108);
	private final int _result;
	
	private ExGoodsInventoryResult(int result)
	{
		_result = result;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xE4);
		writeD(_result);
	}
}