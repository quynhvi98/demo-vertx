package pdf;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.Environment;
import io.micronaut.core.io.ResourceResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import vn.com.mservice.dvcgateway.model.request.PayStatusRequest;
import vn.com.mservice.dvcgateway.model.request.TruyVanGiaoDich;
import vn.com.mservice.dvcgateway.model.response.BienLaiResponse;
import vn.com.mservice.dvcgateway.model.response.PayStatusResponse;
import vn.com.mservice.dvcgateway.service.internal.VerifyPaymentService;
import vn.com.mservice.dvcgateway.util.ApiLogger;
import vn.com.mservice.dvcgateway.util.RSAUtils;

import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;
import static org.thymeleaf.templatemode.TemplateMode.HTML;

public class TestHTMLToPdf {

        private static final String OUTPUT_FILE = "test.pdf";
        private static final String UTF_8 = "UTF-8";
        private final ApiLogger logging = new ApiLogger(this.getClass());
        @Inject
        private VerifyPaymentService verifyPaymentService;

        @Value("${dvc.secret-key}")
        private String secretKey;

        @Value("${dvc.access-key}")
        private String accessKey;

        @Value("${dvc.public-key-path}")
        private String dvcPublicKeyPath;


        @Value("${dvc.public-key}")
        private String dvcPublicKey;


        @Value("${momo.key-store-path}")
        private String mServiceKeyStorePath;

        @Value("${momo.key-store-pass}")
        private String mServiceKeyStorePass;

        @Value("${momo.key-store-alias}")
        private String mServiceAliasName;

        @Inject
        private ResourceResolver resourceResolver;
        @Inject
        private Environment environment;


        public BienLaiResponse fetchTruyVanGiaoDich(TruyVanGiaoDich request) throws Exception {
            BienLaiResponse bienLaiResponse = new BienLaiResponse();
            PayStatusResponse payStatusResponse = buildTransactionProcessor(request);

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(HTML);
            templateResolver.setCharacterEncoding(UTF_8);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            Context context = new Context();
            context.setVariable("payStatusResponse", payStatusResponse);

            payStatusResponse.getMessages();
            payStatusResponse.getStatus();
            payStatusResponse.getData().getCustomerName();

            String renderedHtmlContent = templateEngine.process("views/test", context);
            String xHtml = convertToXhtml(renderedHtmlContent);

//        ITextRenderer renderer = new ITextRenderer();
//
//        renderer.getFontResolver().addFont("src/main/resources/vuArial.ttf", IDENTITY_H, EMBEDDED);

            String baseUrl = FileSystems
                    .getDefault()
                    .getPath("src", "main", "resources")
                    .toUri()
                    .toURL()
                    .toString();
//        renderer.setDocumentFromString(xHtml, baseUrl);
//        renderer.layout();

            // And finally, we create the PDF:
//        OutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
//        renderer.createPDF(outputStream);
//        outputStream.close();

//        String html = "        <td style=\"padding: 18px 18px 9px;line-height: 150%;color: #4d4d4d;font-family: Helvetica;font-size: 18px;text-align: left;\">\n" +
//                "                                                                <strong>Hóa đơn điện tử MoMo</span></strong>\n" +
//                "                                                            </td>";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FILE));
            BaseFont bf = BaseFont.createFont("src/main/resources/vuArial.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
            Font font = new Font(bf,15);
            document.open();
            document.add(new Paragraph(baseUrl, font));
            document.close();


            logging.info("Response parameters: {}", bienLaiResponse);
            return bienLaiResponse;
        }

        public PayStatusResponse buildTransactionProcessor(TruyVanGiaoDich request) throws Exception {
            PayStatusRequest payStatusRequest = new PayStatusRequest();
            payStatusRequest.setPartnerCode(request.getMaDoiTac());
            payStatusRequest.setPartnerRefId(request.getMaThamChieu());
            payStatusRequest.setVersion(2D);
            String jsonStr = payStatusRequest.toStringHash();
            byte[] testByte = jsonStr.getBytes(StandardCharsets.UTF_8);
            payStatusRequest.setHash(RSAUtils.encryptRSA(testByte, dvcPublicKey));
            logging.info("Transaction processor request parameters: {}", payStatusRequest);
            return verifyPaymentService.fetchPayStatus(payStatusRequest).blockingGet();
        }

        private String convertToXhtml(String html) throws UnsupportedEncodingException {
            Tidy tidy = new Tidy();
            tidy.setInputEncoding(UTF_8);
            tidy.setOutputEncoding(UTF_8);
            tidy.setXHTML(true);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            tidy.parseDOM(inputStream, outputStream);
            return outputStream.toString(UTF_8);
        }


}
