package com.vonglasow.michael.satstat.data;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.gsm.GsmCellLocation;

public class CellTowerListGsm extends CellTowerList<CellTowerGsm> {
	/**
	 * Returns the cell tower with the specified data, or {@code null} if it is not in the list. 
	 */
	public CellTowerGsm get(int mcc, int mnc, int lac, int cid) {
		String entry = String.format("%s:%d-%d-%d-%d", CellTowerGsm.FAMILY, mcc, mnc, lac, cid);
		return this.get(entry);
	}
	
	/**
	 * Adds or updates a cell tower.
	 * <p>
	 * If the cell tower is already in the list, it is replaced; if not, a new
	 * entry is created.
	 * <p>
	 * This method will set the cell's identity data. After this call,
	 * {@link #isServing()} will return {@code true} for this cell. 
	 * @param networkOperator The network operator, as returned by {@link android.telephony.TelephonyManager#getNetworkOperator()}.
	 * @param location The {@link android.telephony.GsmCellLocation}, as returned by {@link android.telephony.TelephonyManager#getCellLocation()}.
	 * @return The new or updated entry.
	 */
	public CellTowerGsm update(String networkOperator, GsmCellLocation location) {
		int mcc = Integer.parseInt(networkOperator.substring(0, 3));
		int mnc = Integer.parseInt(networkOperator.substring(3));
		CellTowerGsm result = this.get(mcc, mnc, location.getLac(), location.getCid());
		if (result == null) {
			result = new CellTowerGsm(mcc, mnc, location.getLac(), location.getCid());
			this.put(result.getText(), result);
		}
		result.setCellLocation(true);
		return result;
	}
	
	/**
	 * Adds or updates a cell tower.
	 * <p>
	 * If the cell tower is already in the list, it is replaced; if not, a new
	 * entry is created.
	 * <p>
	 * This method will set the cell's identity data, generation and its signal
	 * strength. 
	 * @return The new or updated entry.
	 */
	public CellTowerGsm update(String networkOperator, NeighboringCellInfo cell) {
		int mcc = Integer.parseInt(networkOperator.substring(0, 3));
		int mnc = Integer.parseInt(networkOperator.substring(3));
		CellTowerGsm result = this.get(mcc, mnc, cell.getLac(), cell.getCid());
		if (result == null) {
			result = new CellTowerGsm(mcc, mnc, cell.getLac(), cell.getCid());
			this.put(result.getText(), result);
		}
		result.setNeighboringCellInfo(true);
		result.setDbm(cell.getRssi() * 2 - 113);
		result.setNetworkType(cell.getNetworkType());
		return result;
	}
	
	/**
	 * Adds or updates a cell tower.
	 * <p>
	 * If the cell tower is already in the list, it is replaced; if not, a new
	 * entry is created.
	 * <p>
	 * This method will set the cell's identity data, its signal strength and
	 * whether it is the currently serving cell. If the API level is 18 or 
	 * higher, it will also set the generation.
	 * @return The new or updated entry.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public CellTowerGsm update(CellInfoGsm cell) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) 
			return null;
		CellIdentityGsm cid = cell.getCellIdentity();
		CellTowerGsm result = this.get(cid.getMcc(), cid.getMnc(), cid.getLac(), cid.getCid());
		if (result == null) {
			result = new CellTowerGsm(cid.getMcc(), cid.getMnc(), cid.getLac(), cid.getCid());
			this.put(result.getText(), result);
		}
		result.setCellInfo(true);
		result.setDbm(cell.getCellSignalStrength().getDbm());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			result.setGeneration(2);
		result.setServing(cell.isRegistered());
		return result;
	}
	
	/**
	 * Adds or updates a cell tower.
	 * <p>
	 * If the cell tower is already in the list, it is replaced; if not, a new
	 * entry is created.
	 * <p>
	 * This method will set the cell's identity data and generation, its signal 
	 * strength and whether it is the currently serving cell. 
	 * @return The new or updated entry.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public CellTowerGsm update(CellInfoWcdma cell) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) 
			return null;
		CellIdentityWcdma cid = cell.getCellIdentity();
		CellTowerGsm result = this.get(cid.getMcc(), cid.getMnc(), cid.getLac(), cid.getCid());
		if (result == null) {
			result = new CellTowerGsm(cid.getMcc(), cid.getMnc(), cid.getLac(), cid.getCid());
			this.put(result.getText(), result);
		}
		result.setCellInfo(true);
		result.setDbm(cell.getCellSignalStrength().getDbm());
		result.setGeneration(3);
		result.setServing(cell.isRegistered());
		return result;
	}
}
