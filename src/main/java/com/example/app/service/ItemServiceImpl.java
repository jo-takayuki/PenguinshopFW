package com.example.app.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.Item;
import com.example.app.domain.Picture;
import com.example.app.mapper.ItemMapper;
import com.example.app.mapper.PictureMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemServiceImpl implements ItemService {

//	private static final String UPLOAD = "C:/Users/zd1P16/uploads";
	private static final String UPLOAD = "/home/trainee/uploads";

	@Autowired
	private ItemMapper mapper;

	@Autowired
	private PictureMapper pMapper;

	@Override
	public List<Item> findAll() throws Exception {
		return mapper.findAll();
	}

	@Override
	public Item findById(Integer id) throws Exception {
		return mapper.findById(id);
	}

	@Override
	public List<Picture> pictureAll(Integer Id) throws Exception {
		return pMapper.pictureAll(Id);
	}

	@Override
	public List<Item> search(String key) throws Exception {
		return mapper.search(key);
	}

	@Override
	public int totalPage(int numPage) throws Exception {
		double totalNum = mapper.count();
		return (int) Math.ceil(totalNum / numPage);
	}

	@Override
	public List<Item> limitItem(int page, int numPage) throws Exception {
		int offset = numPage * (page - 1);
		return mapper.limit(offset, numPage);
	}

	@Override
	public void insert(Item item) throws Exception {
		mapper.insert(item);
		List<MultipartFile> file = (List<MultipartFile>) item.getImageFiles();
		Picture picture = new Picture();
		for (MultipartFile pic : file) {
			picture.setName(pic.getOriginalFilename());
			picture.setItemId(item.getId());
			pMapper.insert(picture);
			File dest = new File(UPLOAD + "/" + pic.getOriginalFilename());
			pic.transferTo(dest);
		}
	}

	@Override
	public void update(Item item) throws Exception {
		mapper.update(item);
		List<MultipartFile> file = (List<MultipartFile>) item.getImageFiles();
		pMapper.delete(item.getId());
		Picture picture = new Picture();
		for (MultipartFile pic : file) {
			picture.setName(pic.getOriginalFilename());
			picture.setItemId(item.getId());
			pMapper.insert(picture);
			File dest = new File(UPLOAD + "/" + pic.getOriginalFilename());
			pic.transferTo(dest);
			System.out.println("update:" + picture);
		}
	}

	@Override
	public void delete(Integer id) throws Exception {
		mapper.delete(id);
	}

	@Override
	public List<Item> findByCompanyId(Integer id) throws Exception {
		return mapper.findByCompanyId(id);
	}

	@Override
	public Item findCart(Integer id) throws Exception {
		return mapper.findCart(id);
	}
}
