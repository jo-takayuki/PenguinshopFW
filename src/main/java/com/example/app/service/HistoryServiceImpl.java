package com.example.app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Cart;
import com.example.app.domain.Company;
import com.example.app.domain.History;
import com.example.app.domain.Item;
import com.example.app.domain.Member;
import com.example.app.domain.Receipt;
import com.example.app.domain.Total;
import com.example.app.mapper.HistoryMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class HistoryServiceImpl implements HistoryService {

	@Autowired
	private HistoryMapper mapper;

	@Autowired
	private ItemService iService;

	@Override
	public List<History> findAll() throws Exception {
		return mapper.findAll();
	}

	@Override
	public List<History> findByIdAndTid(Integer id, Integer tId) throws Exception {
		return mapper.findByIdAndTid(id, tId);
	}

	@Override
	public List<Item> historyList(HttpSession session) throws Exception {
		Member member = (Member) session.getAttribute("member");
		Company company = (Company) session.getAttribute("company");
		List<Item> items = new ArrayList<>();
		if (member != null) {
			Integer id = member.getId();
			Integer tId = member.getTypeId();
			List<History> historyList = mapper.findByIdAndTid(id, tId);
			List<Item> itemList = iService.findAll();
			for (History history : historyList) {
				for (Item item : itemList) {
					if (history.getItemId() == item.getId()) {
						items.add(item);
					}
				}
			}
			return items;
		} else {
			if (company != null) {
				Integer id = company.getId();
				Integer tId = company.getTypeId();
				List<History> historyList = mapper.findByIdAndTid(id, tId);
				List<Item> itemList = iService.findAll();
				for (History history : historyList) {
					for (Item item : itemList) {
						if (history.getItemId() == item.getId()) {
							items.add(item);
						}
					}
				}
				return items;
			}
		}
		return items;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void insert(HttpSession session) throws Exception {
		Member member = (Member) session.getAttribute("member");
		Company company = (Company) session.getAttribute("company");
		List<Cart> cartList = (List<Cart>) session.getAttribute("cartList");
		if (member != null) {
			for (Cart cart : cartList) {
				History history = new History();
				history.setMemComId(member.getId());
				history.setTypeId(member.getTypeId());
				history.setItemId(cart.getId());
				history.setUnit(cart.getUnit());
				mapper.insert(history);
			}
		} else {
			if (company != null) {
				for (Cart cart : cartList) {
					History history = new History();
					history.setMemComId(company.getId());
					history.setTypeId(company.getTypeId());
					history.setItemId(cart.getId());
					history.setUnit(cart.getUnit());
					mapper.insert(history);
				}
			}
		}
	}

	@Override
	public int totalPage(int numPage) throws Exception {
		double totalNum = mapper.count();
		return (int) Math.ceil(totalNum / numPage);
	}

	@Override
	public List<History> limitHistory(int page, int numPage) throws Exception {
		int offset = numPage * (page - 1);
		return mapper.limit(offset, numPage);
	}

	@Override
	public List<History> seles(HttpSession session) throws Exception {
		Member member = (Member) session.getAttribute("member");
		Company company = (Company) session.getAttribute("company");
		List<History> selesList = new ArrayList<>();
		if (member != null) {
			Integer id = member.getId();
			Integer tId = member.getTypeId();
			selesList = mapper.seles(id, tId);
		} else {
			if (company != null) {
				Integer id = company.getId();
				Integer tId = company.getTypeId();
				selesList = mapper.seles(id, tId);
			}
		}
		return selesList;
	}

	@Override
	public Total receipt(HttpSession session, Date created) throws Exception {
		Total retTotal = new Total();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
		SimpleDateFormat cre = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
		String creFormatted = cre.format(created);
		Member member = (Member) session.getAttribute("member");
		Company company = (Company) session.getAttribute("company");
		List<Receipt> receiptList = new ArrayList<>();
		List<Item> itemList = iService.findAll();
		int total = 0;
		if (member != null) {
			Integer id = member.getId();
			Integer tId = member.getTypeId();
			List<History> historyList = mapper.findByIdAndTid(id, tId);
			for (History history : historyList) {
				if (creFormatted.equals(sdf.format(history.getCreated()))) {
					for (Item item : itemList) {
						if (history.getItemId() == item.getId()) {
							String name = item.getName();
							Integer price = item.getPrice();
							Integer unit = history.getUnit();
							Integer money = item.getPrice() * history.getUnit();
							total += money;
							receiptList.add(new Receipt(name, price, unit, money));
						}
					}
				}
			}
			retTotal.setTotal(total);
			retTotal.setReceipts(receiptList);
			return retTotal;
		} else {
			if (company != null) {
				Integer id = company.getId();
				Integer tId = company.getTypeId();
				List<History> historyList = mapper.findByIdAndTid(id, tId);
				for (History history : historyList) {
					if (creFormatted.equals(sdf.format(history.getCreated()))) {
						for (Item item : itemList) {
							if (history.getItemId() == item.getId()) {
								String name = item.getName();
								Integer price = item.getPrice();
								Integer unit = history.getUnit();
								Integer money = item.getPrice() * history.getUnit();
								total += money;
								receiptList.add(new Receipt(name, price, unit, money));
							}
						}
					}
				}
				retTotal.setTotal(total);
				retTotal.setReceipts(receiptList);
				return retTotal;
			}
		}
		return retTotal;
	}

}
