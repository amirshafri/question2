package com.example.demo.service;

import java.util.Date;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Stock;
import com.example.demo.repository.StockRepo;

@Service
public class StockService implements Job {
	static final Logger logger = Logger.getLogger(StockService.class.getName());

	@Autowired
	StockRepo stockRepo;

	public void refreshPricingParameters() {
		logger.info("Start get data:" + new Date());
		try {
			String webPage = "https://klse.i3investor.com/web/index";
			Element html = Jsoup.connect(webPage).get().getElementById("dashboard-col-2");

			String losers = html.childNode(3).childNode(7).childNode(1).childNode(3).childNode(3).childNode(1)
					.outerHtml();
			losers = losers.replace("<p style=\"margin-bottom: 0px; \"> ", "");
			losers = losers.replace(" </p>", "");
			losers = losers.replace("<strong>", "");
			losers = losers.replace("</strong>", "");
			// gainer
			String gainer = html.childNode(3).childNode(7).childNode(1).childNode(1).childNode(3).childNode(1)
					.childNode(1).outerHtml();
			gainer = gainer.replace("<p style=\"margin-bottom: 0px; \"> ", "");
			gainer = gainer.replace(" </p>", "");
			gainer = gainer.replace("<strong>", "");
			gainer = gainer.replace("</strong>", "");
			Stock stock = new Stock();
			stock.setNumGainers(Integer.parseInt(gainer));
			stock.setNumLosers(Integer.parseInt(losers));
			stock.setRecordDatetime(new Date());
			stockRepo.save(stock);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("End data:" + new Date().toString());

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		refreshPricingParameters();
	}

}
