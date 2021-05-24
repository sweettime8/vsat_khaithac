<template>
  <div class="app-container">
    <div class="filter-container">
      <el-row>
          <div class="divBoderGroup"></div>
          <span class="spanTitleGroup">Thông tin chung</span>
          <div class="divBackroColor">
                <div class="form-group">
                    <el-row>
                        <el-col :span="4">
                            
                            <p class="pTitle">MMSI</p>
                            <label class="labelContent">{{editVessel.mmsi}}</label>
                        </el-col>
                        <el-col :span="4">
                            
                            <p class="pTitle">Tên tàu</p>
                            <label class="labelContent">{{editVessel.vesselName}}</label>
                        </el-col>
                        <el-col :span="4">
                            
                            <p class="pTitle">IMO</p>
                            <label class="labelContent">{{editVessel.imo}}</label>
                        </el-col>
                        <el-col :span="4">
                            <p class="pTitle">Đội tàu</p>
                           <label class="labelContent">{{editVessel.crew}}</label>
                        </el-col>              
                    </el-row>
                    <el-row>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Loại cập nhật</p>
                            <label class="labelContent"></label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Thời gian cập nhật</p>
                            <label class="labelContent">{{editVessel.updatedTime | formatDate}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Chiều dài</p>
                            <label class="labelContent">{{editVessel.width}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Chiều rộng</p>
                           <label class="labelContent">{{editVessel.height}}</label>
                        </el-col>              
                    </el-row>
                </div>

            </div>
      </el-row>

      <el-row>
          <div class="" style="margin-bottom:10px"></div>
          <span class="spanTitleGroup">THÔNG TIN KỸ THUẬT</span>
          <div class="divBackroColor">
                <div class="form-group">
                    <el-row>
                        <el-col :span="4">
                           
                            <p class="pTitle">Quốc tịch</p>
                            <label class="labelContent">{{editVessel.countryName}}</label>
                        </el-col>
                        <el-col :span="4">
                            
                            <p class="pTitle">Loại tàu</p>
                            <label class="labelContent">{{editVessel.vesselTypeName}}</label>
                        </el-col>
                        <el-col :span="4">
                            
                            <p class="pTitle">Hô hiệu</p>
                            <label class="labelContent">{{editVessel.callsign}}</label>
                        </el-col>
                        <el-col :span="4">
                            
                            <p class="pTitle">Số điện thoại vệ tinh</p>
                           <label class="labelContent">{{editVessel.satellitePhoneCode}}</label>
                        </el-col>   
                        <el-col :span="4">
                            
                            <p class="pTitle">Mớn nước khi không tải</p>
                           <label class="labelContent">{{editVessel.draugth}}</label>
                        </el-col>             
                    </el-row>

                    <el-row>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Thuyên chuyển</p>
                            <label class="labelContent">{{editVessel.displacement}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Tốc độ trung bình</p>
                            <label class="labelContent">{{editVessel.speedAvg}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Tốc độ tối đa</p>
                            <label class="labelContent">{{editVessel.speedMax}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Tổng tải trọng</p>
                           <label class="labelContent">{{editVessel.grossTonnage}}</label>
                        </el-col> 
                        <el-col :span="4">     
                          <div class="divBoderDetail"></div>
                            <p class="pTitle">Tổng tải trọng khi không tải</p>
                           <label class="labelContent">{{editVessel.deadWeight}}</label>
                        </el-col>          
                    </el-row>
                </div>

            </div>
      </el-row>     

      <el-row>
          <div class="" style="margin-bottom:10px"></div>
          <span class="spanTitleGroup">SỞ HỮU & QUẢN LÝ</span>
          <div class="divBackroColor">
                <div class="form-group">
                    <el-row>
                        <el-col :span="4">
                            
                            <p class="pTitle">Năm sản xuất</p>
                            <label class="labelContent">{{editVessel.yearOfBuild }}</label>
                        </el-col>
                        <el-col :span="4">
                           
                            <p class="pTitle">Nơi sản xuất</p>
                            <label class="labelContent">{{editVessel.placeOfBuildCode}}</label>
                        </el-col>
                        <el-col :span="4">
                           
                            <p class="pTitle">Đơn vị</p>
                            <label class="labelContent">{{editVessel.unit}}</label>
                        </el-col>
                        <el-col :span="4">
                            
                            <p class="pTitle">Loại động cơ</p>
                           <label class="labelContent">{{editVessel.engineType}}</label>
                        </el-col>   
                        <el-col :span="4">
                            
                            <p class="pTitle">Chủ sở hữu</p>
                           <label class="labelContent">{{editVessel.owner}}</label>
                        </el-col>              
                    </el-row>
                    <el-row>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Đơn vị vận hành</p>
                            <label class="labelContent">{{editVessel.operationUnit}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Tình trạng</p>
                            <label class="labelContent">{{editVessel.status}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p class="pTitle">Thông tin khác</p>
                            <label class="labelContent">{{editVessel.otherInfo}}</label>
                        </el-col>             
                    </el-row>
                </div>

            </div>
      </el-row>

      <el-row>
          <div class="" style="margin-bottom:10px"></div>
          <span class="spanTitleGroup">TRUYỀN TÍN AIS (GẦN ĐÂY NHẤT)</span>
          <div class="divBackroColor">
                <div class="form-group">
                    <el-row>
                        <el-col :span="6">
                           
                            <p>Vị trí tàu (Thời gian cuối : 27/01/2021 14:32:22)</p>
                        </el-col>            
                    </el-row>
                    <el-row>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p>Tọa độ</p>
                            <label class="labelContent">{{editVessel.width}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p>Tốc độ</p>
                            <label class="labelContent"></label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p>Hướng đi</p>
                            <label class="labelContent">{{editVessel.width}}</label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p>Trạng thái hàng hải</p>
                           <label class="labelContent">{{editVessel.height}}</label>
                        </el-col>              
                    </el-row>
                </div>

            </div>
            <div class="divBackroColor">
                <div class="form-group">
                    <el-row>
                        <el-col :span="6">
                            
                            <p>Chi tiết hành trình (Thời gian cuối : 27/01/2021 14:32:22)</p>
                        </el-col>            
                    </el-row>
                    <el-row>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p>Đích đến</p>
                            <label class="labelContent"></label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p>Thời gian dự kiến</p>
                            <label class="labelContent"></label>
                        </el-col>
                        <el-col :span="4">
                            <div class="divBoderDetail"></div>
                            <p>Mớn nước</p>
                            <label class="labelContent">{{editVessel.width}}</label>
                        </el-col>             
                    </el-row>
                </div>

            </div>
      </el-row> 
       
      <el-row>
        <div class="" style="margin-bottom:10px"></div>
          <span class="spanTitleGroup">CHI TIẾT HÀNH TRÌNH (GẦN ĐÂY NHẤT)</span>
         <div class="divBackroColorMap">
                  <template>
                      <div id="mapOL"></div>                  
                  </template>                    
                </div>      
      </el-row>          

      <el-row>
          <div class="" style="margin-bottom:10px"></div>
          <span class="spanTitleGroup">THƯ VIỆN ẢNH</span>
          <div class="divBackroColor">
                <div class="form-group">
                  <template>
                    <el-carousel indicator-position="outside">
                      <el-carousel-item v-for="item in images" :key="item.id">
                        <center><img :src="item.imagePath" style="width: auto;max-width: 100%;height: 300px;" v-if="item.imagePath" /></center>
                      </el-carousel-item>
                    </el-carousel>
                  </template>
                    
                </div>

            </div>
      </el-row>

      <el-row>
          <div class="" style="margin-bottom:10px"></div>
          <div class="divBackroColor">
                <div class="form-group">
                    <el-row>
                        <el-col :span="12" style="padding:10px">
                            <span class="spanTitleGroup">DANH SÁCH IP</span>
                            <div class="divBoderDetail"></div>
                                <el-table v-loading="loadingIP" :data="ips" border fit highlight-current-row style="width: 100%">
                                    <el-table-column align="center" label="STT" width="50">
                                    <template slot-scope="scope">
                                      <span>{{
                                        (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
                                      }}</span>
                                    </template>
                                  </el-table-column>
                                  <el-table-column label="IP" width="200">
                                    <template slot-scope="scope">
                                      <span>{{ scope.row.ipAddress }}</span>
                                    </template>
                                  </el-table-column>
                                  <el-table-column label="Ghi chú" >
                                    <template slot-scope="scope">
                                      <span>{{ scope.row.note }}</span>
                                    </template>
                                  </el-table-column>
                                  
                              </el-table>
                              <pagination
                                v-show="totalIP > 10"
                                :total="totalIP"
                                :page.sync="query.currentPage"
                                :limit.sync="query.rowsPerPage"
                                @pagination="getIPByMMSI"
                              />  
                        </el-col>
                        <el-col :span="12" style="padding:10px;">
                            <span class="spanTitleGroup">DANH SÁCH SĐT VỆ TINH</span>
                            <div class="divBoderDetail"></div>
                              <el-table
                                v-loading="loadingSDT"
                                :data="phones"
                                border
                                fit
                                highlight-current-row
                                style="width: 100%"
                              >
                                  <el-table-column align="center" label="STT" width="50">
                                  <template slot-scope="scope">
                                    <span>{{
                                      (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
                                    }}</span>
                                  </template>
                                </el-table-column>
                                <el-table-column label="SĐT vệ tinh" width="200">
                                  <template slot-scope="scope">
                                    <span>{{ scope.row.phoneNumber }}</span>
                                  </template>
                                </el-table-column>
                                <el-table-column label="Ghi chú">
                                  <template slot-scope="scope">
                                    <span>{{ scope.row.note }}</span>
                                  </template>
                                </el-table-column>
                            </el-table>
                            <pagination
                              v-show="totalSDT > 10"
                              :total="totalSDT"
                              :page.sync="query.currentPage"
                              :limit.sync="query.rowsPerPage"
                              @pagination="getSDTByMMSI"
                            />          
                        </el-col>          
                    </el-row>
                    
                </div>

            </div>
      </el-row>

    </div>


  </div>
</template>

<script>

import Pagination from "@/components/Pagination";
import waves from "@/directive/waves"; // Waves directive
import Resource from "@/api/resource";
import { mapGetters } from 'vuex';

 const vesselDetailResource = new Resource("contact/vessel/detail/getDetailVessel")
// const vesselResource = new Resource("ais/vessel/search-list");


//map
import Map from "ol/Map.js";
import View from "ol/View.js";

import ImageWMS from "ol/source/ImageWMS";
import { OSM, Vector as VectorSource } from "ol/source";
import { fromLonLat } from "ol/proj";
import VectorLayer from "ol/layer/Vector";
import { Image as ImageLayer } from "ol/layer";
import { Tile as TileLayer, Vector } from "ol/layer";
import GeoJSON from "ol/format/GeoJSON";
import XYZ from "ol/source/XYZ";
import Point from "ol/geom/Point";
import TileWMS from "ol/source/TileWMS";
import Draw from "ol/interaction/Draw";
import { LineString } from "ol/geom";
import Feature from "ol/Feature";
import Circle from "ol/geom/Circle";
import { Icon, Style, Stroke } from "ol/style";
const hanoi = [105.804817, 21.028511];
let map;
let source;
let currentDraw;
const hanoiWebMercator = fromLonLat(hanoi);

var geojsonObject = {
  'type': 'FeatureCollection',
  'crs': {
    'type': 'name',
    'properties': {
      'name': 'EPSG:3857'
    },
  },
  'features': [
    {
      'type': 'Feature',
      'geometry': {
        'type': 'LineString',
        'coordinates': [
          [120, 20],
          [110, 10],
          [112, 15],
          [115, 12]
          ],
      },
    }]
};
function createStyle(src, img) {
  return new Style({
    image: new Icon({
      anchor: [0.5, 0.96],
      crossOrigin: "anonymous",
      src: src,
      img: img,
      imgSize: [50, 50],
    }),
  });
}

var styleFunction = function (feature) {
  var geometry = feature.getGeometry();
  var styles = [
    // linestring
    new Style({
      stroke: new Stroke({
        color: "red",
        width: 2,
      }),
    }),
  ];

  geometry.forEachSegment(function (start, end) {
    var dx = end[0] - start[0];
    var dy = end[1] - start[1];
    var rotation = Math.atan2(dy, dx);
    var rotationx = Math.atan2(dx, dy);
    // arrows
    styles.push(
      new Style({
        geometry: new Point(end),
        image: new Icon({
          src: 'http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png',
          anchor: [0.5, 0.5],
          
          scale:2,
          rotateWithView: true,
          rotation: -rotation,
        }),
      })
    );
    styles.push(
      new Style({
        geometry: new Point(start),
        image: new Icon({
          src: 'http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png',
          anchor: [0.5, 0.5],
          
          scale:2,
          rotateWithView: true,
          rotation: -rotation,
        }),
      })
    );
  });

  return styles;
};

//end map


export default {
    mounted() {
    source = new VectorSource({ wrapX: false });

    var vector = new VectorLayer({
      source: source,
    });

    map = new Map({
      target: "mapOL",
      layers: [
        new TileLayer({
          source: new TileWMS({
            url: "http://192.168.61.106:3691/geoserver/wms",
            params: {
              LAYERS: "EyeseaDelta",
              TILED: true,
              VERSION: "1.1.1",
              FORMAT: "image/jpeg",
              EPSG: 4326,
            },
            serverType: "geoserver",
          }),
        }),
        vector,
      ],
      view: new View({
        center: hanoiWebMercator,
        zoom: 5,
      }),
    });
  },
  components: {
    Pagination,
  
    //AdvancedSelect ,
  },
  directives: { waves },
  data() {
    return {
       mmsi : 0,
       total: 0,
       loadingSDT: true,
       loadingIP: true,
       totalSDT:0,
       totalIP:0,
       ips:null,
       phones: null,
       images :null,
       query: {
        currentPage: 1,
        rowsPerPage: 15,
        sort: "createdAt",
        keyword: "",
      },

      editVessel: {
        mmsi:0,
        vesselName : '',           //tên tàu
        vesselTypeName :'',
        imo: 0,
        crew: '',                  //đội tàu
        satellitePhoneCode :'',    //số điện thoại vệ tinh
        countryId: 0,
        countryName :'',
        width : 0,
        height : 0,
        callsign: '',
        createdTime: null,      
        updatedTime: null,         //Thời gian cập nhật 
        nameStatic: null,     
        draugth : 0,               // Mớn nước khi không tải  
        displacement : '',         //Thuyên chuyển
        speedAvg : 0 ,             //Tốc độ trung bình (knot)
        speedMax : 0 ,             //Tốc độ tối đa (knot)
        grossTonnage : 0 ,         // Tổng Tải trọng 
        yearOfBuild : null ,       //năm sản xuất
        placeOfBuildCode : null ,  // nơi sản xuất
        unit : '',                 //đơn vị  (thiếu api)  -> vessel_alt_info 
        owner : '',                //chủ sở hữu (thiếu api) -> vessel_alt_info 
        operationUnit :'',         //Đơn vị vận hành (thiếu api) -> vessel_alt_info 
        otherInfo : '',            //Thông tin khác
        engineType : '',           //Loại động cơ
        status : '',               //tình trạng (thiếu api) -> vessel_alt_info 
        ips:null,
        phones: null,
        deadWeight: 0,             // Tải trọng khi không tải (tấn)

        
      },

    };
  },
  computed: {
    ...mapGetters([
      'userId',
      'userName',
      'name',
      'avatar',
      'roles',
    ]),
  },
  created() {
       this.mmsi = this.$route.query.mmsi;
       this.getVesselDetail();
  },
  methods: {
      async getVesselDetail(){
            if(this.mmsi)
            {
                let param = {
                    mmsi: this.mmsi
                }
                console.log("mrd param " + this.mmsi);
                await vesselDetailResource.listByPost(param).then((response) => {               
                if (response.status === 200) {
                    this.editVessel = {
                    mmsi : response.data.info.mmsi, 
                    vesselName : response.data.info.name, 
                    imo : response.data.info.imo, 
                    crew : response.data.info.crew, 
                    width : response.data.info.chieudai,
                    height : response.data.info.chieurong,  
                    countryId : response.data.info.country_id, 
                    countryName : response.data.info.countryName,  
                    satellitePhoneCode : response.data.info.satellitePhoneCode,
                    vesselTypeName : response.data.info.vesselTypeName,
                    callsign : response.data.info.callsign,
                    draugth : response.data.info.draugth,
                    displacement : response.data.info.displacement , 
                    speedAvg : response.data.info.speedAvg,
                    speedMax : response.data.info.speedMax,
                    grossTonnage : response.data.info.grossTonnage,       
                    createdTime : response.data.info.createdTime,  
                    updatedTime : response.data.info.updatedTime,            
                    };   
                    this.ips = response.data.ips;
                    this.phones = response.data.phones;
                    this.images = response.data.images;
                    if(this.images.length >0){
                      for(var i=0; i< this.images.length;i++){
                        this.images[i].imagePath = process.env.VUE_APP_FILE_API + "upload" + this.images[i].imagePath;
                      }
                    }

                    

                    console.log("handleChangeVessel :" + response.data);
                }
                })
                .catch((err) => {
                console.log(err);
                });     
                this.loadingIP = false;   
                this.loadingSDT = false;  
            }
      },
      getIPByMMSI(){
        console.log("getIPByMMSI");
      },
      getSDTByMMSI(){
        console.log("getSDTByMMSI");
      },
      handleDraw() {
      currentDraw = new Draw({
        source: source,
        type: "LineString",
      });
      map.addInteraction(currentDraw);
      },
      handleEndDraw() {
        map.removeInteraction(currentDraw);
      },
      handlePolyLineDraw() {
        var vectorSource = new VectorSource({
          features: new GeoJSON().readFeatures(geojsonObject,{ 
          dataProjection: 'EPSG:4326',
          featureProjection:'EPSG:3857' })
        });

        var vectorLayer = new VectorLayer({
          source: vectorSource,
          style: styleFunction,
        });
        map.addLayer(vectorLayer);
      },
  }
};
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;
  .card-panel-col {
    margin-bottom: 30px;
  }
  .card-panel {
    padding-bottom: 25px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #000;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
        background: #40c9c6;
      }
      .icon-message {
        background: #36a3f7;
      }
      .icon-money {
        background: #f4516c;
      }
      .icon-shopping {
        background: #34bfa3;
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shopping {
      color: #34bfa3;
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: left;
      font-weight: bold;
      margin: 25px 0 20px 15px;
      .card-panel-text {
        line-height: 18px;
        color: #000;
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
    .card-panel-detail {
      float: left;
      width: 100%;
      padding: 0 15px;
      margin-bottom: 5px;
    }
    .el-tag {
      width: 100% !important;
      text-align: center !important;
    }
  }
}
.no-margin-top {
  margin-top: 0;
}
.search {
  margin-left: 15px;
}
.block {
  display: inline-block;
  //float: left;
  margin-right: 10px;
}
.blue {
  color: green;
}
.red {
  color: red;
}
.add-value {
  text-align: right;
}
.list-detail .item {
  margin-bottom: 10px;
  .label {
    display: inline-block;
    width: 100px;
  }
  img {
    max-width: 100%;
    display: inline-block;
  }
}

.el-input-number{
  width: 100%;
}
@import url("//unpkg.com/element-ui@2.15.1/lib/theme-chalk/index.css");
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}
.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
.divBoderGroup {
    width: 100%;
    height: 1px;
    margin-top: 5px;
    margin-bottom: 10px;
    border-top: 3px solid #c9c7c7;
}
.divBackroColor {
    padding: 10px;
    background-color: #f6f6f6;
    border: 1px solid #ddd;
    margin-top: 15px;
}
.divBackroColorMap {
    border: 1px solid #ddd;
    margin-top: 15px;
}
.form-group {
    margin-bottom: 1rem;
}
.divBoderDetail {
    height: 1px;
    padding-bottom: 5px;
    margin-top: 5px;
    border-top: 1px solid #dddddd;
}
.pTitle{
  font-size: 12px;
  margin-top: 0;
  margin-bottom: 1rem;
  color: #464855 !important;
}
.labelContent{
  font-weight: bold;
  display: inline-block;
  margin-bottom: 0.5rem;
  color: #464855 !important;
}
.divBoderMap{
  width: 100%;
  height: 300px;
}



</style>



<style lang="scss">
.form-value {
  .el-input__inner {
    margin-bottom: 10px;
  }
}
.claim td .el-button {
  margin-left: 0;
}
.spanTitleGroup {
    margin-top: 15px;
    text-transform: uppercase;
    font-size: 15px;
    font-weight: bold;
    color: #121c4a;
}
@import "~ol/ol.css";
#mapOL {
  width: 100%;
  height: 500px;
}
</style>
