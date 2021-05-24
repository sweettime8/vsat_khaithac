import request from '@/utils/request';
import Resource from "@/api/resource";
/**
 * Simple RESTful resource class
 */

var host=process.env.VUE_APP_BASE_API;

class OverallApi extends Resource{
  constructor(uri) {
    
    super(uri);
    this.uri = uri;
  }

  postApi(url,resource) {
    return request({
      url: url,
      method: 'post',
      data: resource,
    });
  }


  searchVesselGroup(url,resource) {
    return request({
      url: url,
      method: 'post',
      data: resource,
    });
  }

  getListComment(url,resource) {
    return request({
      url: url,
      method: 'post',
      data: resource,
    });
  }

  getVesselDetail(url,resource) {
    return request({
      url: url,
      method: 'post',
      data: resource,
    });
  }

  searchListAis(url,resource) {
    return request({
      url: url,
      method: 'post',
      data: resource,
    });
  }
  saveDraw(url,data) {
    return request({
      url: url,
      method: 'post',
      data: data,
    });
  }

  deleteArea(url,data) {
    return request({
      url: url,
      method: 'post',
      data: data,
    });
  }

  getListArea(url,data) {
    return request({
      url: url,
      method: 'post',
      data: data,
    });
  }
}

export { OverallApi as default };
