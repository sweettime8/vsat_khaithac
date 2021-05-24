import request from '@/utils/request';

/**
 * Simple RESTful resource class
 */
class Resource {
  constructor(uri) {
    this.uri = uri;
  }
  list(query) {
    return request({
      url: '/' + this.uri,
      method: 'get',
      params: query,
    });
  }
  listByPost(resource) {
    return request({
      url: '/' + this.uri,
      method: 'post',
      data: resource,
    });
  }
  get(id) {
    return request({
      url: '/' + this.uri + '/' + id,
      method: 'get',
    });
  }
  store(resource) {
    return request({
      url: '/' + this.uri,
      method: 'post',
      data: resource,
    });
  }
  update(id, resource) {
    return request({
      url: '/' + this.uri + '/' + id,
      method: 'put',
      data: resource,
    });
  }

  updateDucnh(resource) {
    return request({
      url: '/' + this.uri ,
      method: 'put',
      data: resource,
    });
  }

  updateWithResource(resource) {
    return request({
      url: '/' + this.uri ,
      method: 'put',
      data: resource,
    });
  }

  destroy(id) {
    return request({
      url: '/' + this.uri + '/' + id,
      method: 'delete',
    });
  }

  post(resource) {
    return request({
      url: '/' + this.uri,
      method: 'post',
      data: resource,
    });
  }
}

export { Resource as default };
