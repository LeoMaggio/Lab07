package it.polito.tdp.poweroutages.model;

import java.util.*;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {
	private PowerOutageDAO dao;
	private List<Nerc> nlist;
	private Map<Integer, Nerc> nmap;
	
	private List<PowerOutage> elist;
	private List<PowerOutage> soluzione;
	private int maxAffectedPeople;
	
	public Model() {
		dao = new PowerOutageDAO();
		this.nmap = new HashMap<>();
		this.nlist = this.dao.getNercList(nmap);
	}
	
	public List<Nerc> getNercList() {
		return this.nlist;
	}

	public List<PowerOutage> computeWorstCase(Nerc nerc, Integer anni, Integer ore) {
		this.soluzione = new ArrayList<>();
		this.elist = this.dao.getPowerOutagesList(nerc);
		Collections.sort(elist);
		recursive(new ArrayList<PowerOutage>(), anni, ore);
		return soluzione;
	}

	private void recursive(ArrayList<PowerOutage> parziale, Integer anni, Integer ore) {
		if(sumAffectedPeople(parziale) > this.maxAffectedPeople) {
			this.maxAffectedPeople = sumAffectedPeople(parziale);
			soluzione = new ArrayList<PowerOutage>(parziale);
		}
		
		for(PowerOutage po : elist) {
			if(!parziale.contains(po)) {
				parziale.add(po);
				if(checkMaxYears(parziale, anni) && checkMaxHours(parziale, ore))
					recursive(parziale, anni, ore);
				parziale.remove(po);
			}
		}
	}

	private boolean checkMaxHours(List<PowerOutage> parziale, Integer ore) {
		if(this.sumOutageHours(parziale) > ore)
			return false;
		return true;
	}

	private boolean checkMaxYears(List<PowerOutage> parziale, Integer anni) {
		if(parziale.size() > 1) {
			int y1 = parziale.get(0).getYear();
			int y2 = parziale.get(parziale.size() -1).getYear();
			if((y2 - y1 +1) > anni)
				return false;
		}
		return true;
	}

	public int sumAffectedPeople(List<PowerOutage> parziale) {
		int sum = 0;
		for(PowerOutage po : parziale)
			sum += po.getCustomers_affected();
		return sum;
	}

	public int sumOutageHours(List<PowerOutage> parziale) {
		int sum = 0;
		for(PowerOutage po : parziale)
			sum += po.getOutageDuration();
		return sum;
	}

}
