package com.saikrupa.app.service;

import java.util.List;

import com.saikrupa.app.dto.MachineData;

public interface MachineService {
	public List<MachineData> getMachines();
	public MachineData getMachineByCode(final int machineCode);
}
