package com.avp.nj.sup.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.avp.nj.sup.entity.Model;
import com.avp.nj.sup.entity.SubModel;

public class ModelUtil {

	public String[] modelStr = new String[] { "1/正常运营模式", "2/降级运营模式",
			"3/紧急运营模式" };
	public String[] submodelStr = new String[] { "0/停止服务模式/1", "1/正常服务模式/1",
			"2/关闭服务模式/1", "3/临时关站/2", "4/进站免检模式/2", "5/出站免检模式/2", "6/时间日期免检/2",
			"7/车费免检模式/2", "8/列车故障模式/2", "9/紧急放行模式/3" };

	public ModelUtil() {
		super();
	}

	public ArrayList<Model> getModel() {

		ArrayList<Model> ret = new ArrayList<Model>();

		for (int i = 0; i < modelStr.length; i++) {
			String str = modelStr[i];

			Model model = new Model();
			// 解析str
			String[] subStr = new String[2];
			subStr = str.split("/");
			String modelId = subStr[0];

			model.setId(modelId);
			model.setName(subStr[1]);

			ArrayList<SubModel> subModelList = new ArrayList<SubModel>();

			for (int j = 0; j < submodelStr.length; j++) {
				String[] subModel = submodelStr[j].split("/");
				String subModelId = subModel[0];
				String subModelName = subModel[1];
				String new_modelId = subModel[2];
				if (modelId.equals(new_modelId)) {

					SubModel sModel = new SubModel();
					sModel.setId(subModelId);
					sModel.setName(subModelName);
					subModelList.add(sModel);

				}

			}

			model.setModes(subModelList);
			ret.add(model);
		}
		return ret;
	}
}
