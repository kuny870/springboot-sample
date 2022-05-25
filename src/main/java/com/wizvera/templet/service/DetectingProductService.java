package com.wizvera.templet.service;

import com.wizvera.templet.model.DetectingProduct;
import com.wizvera.templet.model.DetectingProductImage;
import com.wizvera.templet.model.DetectingSearchKeyword;
import com.wizvera.templet.repository.DetectingProductImageRepository;
import com.wizvera.templet.repository.DetectingProductRepository;
import com.wizvera.templet.repository.DetectingSearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class DetectingProductService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DetectingProductRepository detectingProductRepository;

    private final DetectingSearchKeywordRepository detectingSearchKeywordRepository;

    private final DetectingProductImageRepository detectingProductImageRepository;

    /**
     * 가품탐지할 상품 전체 불러오기 Paging
     * @return
     */
    public Page<DetectingProduct> getDetectingProductList(PageRequest pageRequest) {
        return detectingProductRepository.findAll(pageRequest);
    }

    /**
     * 가품탐지할 제품 등록
     * @param detectingProduct
     * @return
     */
    @Transactional
    public void regist(DetectingProduct detectingProduct, MultipartHttpServletRequest mhsr) throws IOException {

        DetectingProduct dp = detectingProductRepository.save(detectingProduct);

        for(int i = 0; i < detectingProduct.getSearchKeyword().size(); i++){
            DetectingSearchKeyword dsk = new DetectingSearchKeyword();
            dsk.setDetectingProductId(dp.getId());
            dsk.setSearchKeyword(detectingProduct.getSearchKeyword().get(i).toString());
            detectingSearchKeywordRepository.save(dsk);
        }

        // 파일 이름을 업로드 한 날짜로 바꾸어서 저장할 것이다
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        // 프로젝트 폴더에 저장하기 위해 절대경로를 설정 (Window 의 Tomcat 은 Temp 파일을 이용한다)
//        String absolutePath = new File("").getAbsolutePath() + "\\";

        // 경로를 지정하고 그곳에다가 저장한다
//        String path = "src/main/resources/static/images/product";
        String path = mhsr.getSession().getServletContext().getRealPath("resources/static/images/product");
        File f = new File(path);
        // 저장할 위치의 디렉토리가 존재하지 않을 경우
        if(!f.exists()){
            // mkdir() 함수와 다른 점은 상위 디렉토리가 존재하지 않을 때 그것까지 생성
            f.mkdirs();
        }

        String fileOriginName = "";

        for(int i = 0; i < detectingProduct.getFile().size(); i++) {
            fileOriginName = detectingProduct.getFile().get(i).getOriginalFilename();

            logger.info("기존 파일명 : " + fileOriginName);
            SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMDD_HHMMSS_" + i);
            Calendar now = Calendar.getInstance();

            //확장자명
            String extension = fileOriginName.substring(fileOriginName.lastIndexOf('.'));

            //fileOriginName 에 날짜 +.+ 확장자명으로 저장시킴.
            fileOriginName = formatter.format(now.getTime()) + extension;
            logger.info("변경된 파일명 : " + fileOriginName);

            f = new File(path + File.separator + fileOriginName);
            detectingProduct.getFile().get(i).transferTo(f);

            DetectingProductImage dpi = new DetectingProductImage();
            dpi.setDetectingProductId(dp.getId());
            dpi.setType("0");
            dpi.setProductImage(fileOriginName);
            detectingProductImageRepository.save(dpi);
        }

    }


    /**
     * 가품탐지할 제품 수정
     * @param detectingProduct
     * @return
     */
    @Transactional
    public void update(DetectingProduct detectingProduct, MultipartHttpServletRequest mhsr) throws IOException  {

        DetectingProduct dp = detectingProductRepository.save(detectingProduct);

        detectingSearchKeywordRepository.deleteByDetectingProductId(dp.getId());
        detectingProductImageRepository.deleteByDetectingProductId(dp.getId());

        for(int i = 0; i < detectingProduct.getSearchKeyword().size(); i++){
            DetectingSearchKeyword dsk = new DetectingSearchKeyword();
            dsk.setDetectingProductId(dp.getId());
            dsk.setSearchKeyword(detectingProduct.getSearchKeyword().get(i).toString());
            detectingSearchKeywordRepository.save(dsk);
        }

        // 파일 이름을 업로드 한 날짜로 바꾸어서 저장할 것이다
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        // 프로젝트 폴더에 저장하기 위해 절대경로를 설정 (Window 의 Tomcat 은 Temp 파일을 이용한다)
//        String absolutePath = new File("").getAbsolutePath() + "\\";

        // 경로를 지정하고 그곳에다가 저장한다
//        String path = "src/main/resources/static/images/product";
        String path = mhsr.getSession().getServletContext().getRealPath("resources/static/images/product");
        File f = new File(path);
        String fileOriginName = "";

        for(int i = 0; i < detectingProduct.getFile().size(); i++) {
            fileOriginName = detectingProduct.getFile().get(i).getOriginalFilename();

            logger.info("기존 파일명 : " + fileOriginName);
            SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMDD_HHMMSS_" + i);
            Calendar now = Calendar.getInstance();

            //확장자명
            String extension = fileOriginName.substring(fileOriginName.lastIndexOf('.'));

            //fileOriginName에 날짜+.+확장자명으로 저장시킴.
            fileOriginName = formatter.format(now.getTime()) + extension;
            logger.info("변경된 파일명 : " + fileOriginName);

            f = new File(path + File.separator + fileOriginName);
            detectingProduct.getFile().get(i).transferTo(f);

            DetectingProductImage dpi = new DetectingProductImage();
            dpi.setDetectingProductId(dp.getId());
            dpi.setType("0");
            dpi.setProductImage(fileOriginName);
            detectingProductImageRepository.save(dpi);
        }

    }


    /**
     * 가품탐지할 제품 삭제
     * @param id
     * @return
     */
    public DetectingProduct delete(Long id) {

        Optional<DetectingProduct> optionalDetectingProduct = detectingProductRepository.findById(id);
        DetectingProduct getDetectingProduct = optionalDetectingProduct.get();
        getDetectingProduct.setDelYn("Y");

        return detectingProductRepository.save(getDetectingProduct);
    }

}

