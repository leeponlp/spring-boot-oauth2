/**
Copyright (c) 2016. 上海趣医网络科技有限公司 版权所有
Shanghai QuYi Network Technology Co., Ltd. All Rights Reserved.

This is NOT a freeware,use is subject to license terms.
*/

package com.qy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qy.mapper.UserInfoMapper;
import com.qy.po.UserInfo;
import com.qy.service.UserInfoService;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年3月31日 下午6:07:17   
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
	
	@Autowired
	UserInfoMapper userInfoMapper;

	@Override
	public UserInfo getUserInfoById(Integer id) {
		// TODO Auto-generated method stub
		return userInfoMapper.getUserInfoById(id);
	}

}
