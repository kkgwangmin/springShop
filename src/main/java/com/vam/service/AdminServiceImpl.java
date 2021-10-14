package com.vam.service;

import com.vam.mapper.AdminMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminMapper adminMapper;

	// 상품 등록
	@Transactional
	@Override
	public void bookEnroll(BookVO book) {
		log.info("(service)bookEnroll.............");

		adminMapper.bookEnroll(book);

		if(book.getImageList() == null || book.getImageList().size() <= 0) {
			return;
		}

		book.getImageList().forEach(attach -> {
			attach.setBookId(book.getBookId());
			adminMapper.imageEnroll(attach);
		});
	}

	// 카테고리 리스트
	@Override
	public List<CateVO> cateList() {

		log.info("(service)cateList..............");

		return adminMapper.cateList();
	}

	// 상품 리스트
	@Override
	public List<BookVO> goodsGetList(Criteria cri) {

		log.info("goodsGetList()...............");
		return adminMapper.goodsGetList(cri);
	}

	// 상품 총 갯수
	@Override
	public int goodsGetTotal(Criteria cri) {
		log.info("goodsGetTotal().........");
		return adminMapper.goodsGetTotal(cri);
	}

	// 상품 상세조회 페이지
	@Override
	public BookVO goodsGetDetail(int bookId) {

		log.info("(service)bookGetDetail......." + bookId);

		return adminMapper.goodsGetDetail(bookId);
	}

	// 상품 정보 수정
	@Transactional
	@Override
	public int goodModify(BookVO vo) {

		log.info("goodsModify........");

		int result = adminMapper.goodsModify(vo);

		if (result == 1 && vo.getImageList() != null && vo.getImageList().size() > 0) {
			adminMapper.deleteImageAll(vo.getBookId());

			vo.getImageList().forEach(attach -> {

				attach.setBookId(vo.getBookId());
				adminMapper.imageEnroll(attach);

			});
		}

		return result;
	}

	// 상품 정보 삭제
	@Override
	@Transactional
	public int goodsDelete(int bookId) {

		log.info("goodsDelete..........");

		adminMapper.deleteImageAll(bookId);

		return adminMapper.goodsDelete(bookId);
	}

	/* 지정 상품 이미지 정보 얻기 */
	@Override
	public List<AttachImageVO> getAttachInfo(int bookId) {

		log.info("getAttachInfo........");

		return adminMapper.getAttachInfo(bookId);
	}

}