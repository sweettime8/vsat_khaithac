<template>
  <div style="width: 100%; height: 100%">
    <!-- Popup -->
    <div id="popup" class="ol-popup dialog-detail-vessel">
      <a
        href="#"
        id="popup-closer"
        class="ol-popup-closer"
        @click="closerPopup()"
      ></a>
      <div class="popup-header">
        <p
          style="
            color: #fff;
            font-size: 18px;
            line-height: 36px;
            padding-left: 11px;
          "
        >
          Đối tượng:
          {{
            vesselDetailDialog.objId
              ? vesselDetailDialog.objId
              : objectDetailDialog.objId
          }}
        </p>
      </div>
      <div id="popup-content" :style="[objectDetailDialog.isUfo == 1?{'height': '345px !important'}:{}]" v-show="isShowVesselDetailDialog">
        <b-row>
          <b-col sm="4" style="text-align: center">
            <img
              :src="
                vesselDetailDialog.imagePath == null
                  ? ''
                  : vesselDetailDialog.imagePath
              "
              @error="setAltImg($event)"
              width="80"
              height="80"
            />
          </b-col>
          <b-col
            sm="20"
            style="max-width: 250px; overflow: auto; max-height: 91px"
          >
            <b-row>
              <span
                ><b>Quốc gia: </b>
                <span>{{
                  vesselDetailDialog.countryName
                    ? vesselDetailDialog.countryName
                    : objectDetailDialog.countryName
                }}</span>
              </span>
            </b-row>
            <b-row>
              <span
                ><b>Tên đối tượng: </b>
                <span>{{
                  vesselDetailDialog.vesselName
                    ? vesselDetailDialog.vesselName
                    : objectDetailDialog.name
                }}</span>
              </span>
            </b-row>
            <!-- <b-row>
              <span
                ><b>Trạng thái: </b>
                <span :style="[vesselDetailDialog.status==1 ? { color: '#28a12c' } : { color: 'red' }]"
                  ><b>{{ vesselDetailDialog.status==1?'Đang hoạt động':'Không hoạt động' }}</b></span
                >
              </span>
            </b-row> -->
          </b-col>
        </b-row>
        <b-tabs content-class="mt-3" v-model="stepTabDetail">
          <b-tab title="Thông tin chung" active >
            <div v-if="vesselDetailDialog.isUfo == 0">
              <!-- <label>{{vesselDetailDialog.isUfo}}</label> -->

              <b-row class="table-detail">
                <b-col sm="12" class="text-des">
                  <span
                    ><b>Loại tàu: </b>
                    <span
                      id="vesselType"
                      v-tooltip="{ content: vesselDetailDialog.vesselTypeName }"
                      >{{ vesselDetailDialog.vesselTypeName }}</span
                    >
                  </span>
                </b-col>
              </b-row>

              <b-row>
                <b-col sm="6" class="table-detail text-des">
                  <span
                    ><b>Dài: </b>
                    <span>{{ vesselDetailDialog.length }}</span>
                  </span>
                </b-col>
                <b-col sm="6" class="text-des table-detail">
                  <span
                    ><b>rộng: </b>
                    <span id="vesselType">{{ vesselDetailDialog.width }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="6" class="table-detail text-des">
                  <span
                    ><b>Mớm nước: </b>
                    <span>{{ vesselDetailDialog.draugth }} (m)</span>
                  </span>
                </b-col>
                <b-col sm="6" class="table-detail">
                  <span
                    ><b>Hô hiệu: </b>
                    <span id="vesselType">{{
                      vesselDetailDialog.callSign
                    }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="6" class="table-detail text-des">
                  <span
                    ><b>Tốc độ: </b>
                    <span>{{ vesselDetailDialog.sog }} (knot)</span>
                  </span>
                </b-col>
                <b-col sm="6" class="table-detail text-des">
                  <span
                    ><b>Hướng: </b>
                    <span id="vesselType"
                      >{{ vesselDetailDialog.cog }} (°)</span
                    >
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="6" class="table-detail text-des">
                  <span
                    ><b>Vĩ độ: </b>
                    <span>{{ vesselDetailDialog.latitude }}</span>
                  </span>
                </b-col>
                <b-col sm="6" class="table-detail text-des">
                  <span
                    ><b>Kinh độ: </b>
                    <span id="vesselType">{{
                      vesselDetailDialog.longitude
                    }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="6" class="table-detail text-des">
                  <span
                    ><b>IPnguồn: </b>
                    <span>{{ vesselDetailDialog.sourceIp }}</span>
                  </span>
                </b-col>
                <b-col sm="6" class="table-detail">
                  <span
                    ><b>IP Đích: </b>
                    <span id="vesselType">{{ vesselDetailDialog.destIp }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="12" class="table-detail">
                  <span
                    ><b>Chiều dữ liệu: </b>
                    <span>{{
                      vesselDetailDialog.direction == 1
                        ? "Chiều đi"
                        : vesselDetailDialog.direction == 2
                        ? "Chiều về"
                        : "không xác định"
                    }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="12" class="table-detail text-des">
                  <span
                    ><b>Cập nhật: </b>
                    <span>{{
                      vesselDetailDialog.ingestTime | formatDate
                    }}</span>
                  </span>
                </b-col>
              </b-row>
   
              <b-row style="margin-top:12px">
                <b-col sm="2" style="padding-left: 0; padding-right: 0">
                </b-col>
                <b-col sm="4" style="padding-left: 0; padding-right: 0">
                  <el-button
                    type="primary"
                    size="small"
                    style="height:35px"
                    @click="openDialogAddToGroup(vesselDetailDialog)"
                    >Thêm vào nhóm</el-button
                  >
                </b-col>
                <b-col sm="4" style="padding-left: 0; padding-right: 0">
                  <el-button
                    type="primary"
                    size="small"
                    style="height:35px"
                    @click="handleGetVoyageDetail(vesselDetailDialog)"
                    >Xem hành trình</el-button
                  >
                </b-col>
              </b-row>
            </div>
            <div v-if="objectDetailDialog.isUfo == 1">
              <b-row class="table-detail">
                <b-col sm="12" class="text-des">
                  <span
                    ><b>Loại đối tượng: Đối tượng không xác định</b>
                    <!-- <span
                      id="vesselType"
                      v-tooltip="{ content: vesselDetailDialog.vesselTypeName }"
                      >{{ vesselDetailDialog.vesselTypeName }}</span
                    >-->
                  </span>
                </b-col>
              </b-row>
              <!-- <b-row>
                <b-col sm="6" class="table-detail">
                  <span
                    ><b>Mớm nước: </b>
                    <span>{{ objectDetailDialog.draugth }} (m)</span>
                  </span>
                </b-col>
                <b-col sm="6" class="text-des table-detail">
                  <span
                    ><b>Hô hiệu: </b>
                    <span id="vesselType">{{
                      objectDetailDialog.callSign
                    }}</span>
                  </span>
                </b-col>
              </b-row> 
              <b-row>
                <b-col sm="6" class="text-des table-detail">
                  <span
                    ><b>Tốc độ: </b>
                    <span>{{ objectDetailDialog.sog }} (knot)</span>
                  </span>
                </b-col>
                <b-col sm="6" class="text-des table-detail">
                  <span
                    ><b>Hướng: </b>
                    <span id="vesselType"
                      >{{ objectDetailDialog.cog }} (°)</span
                    >
                  </span>
                </b-col>
              </b-row>-->
              <b-row>
                <b-col sm="6" class="text-des table-detail">
                  <span
                    ><b>Vĩ độ: </b>
                    <span>{{ objectDetailDialog.latitude }}</span>
                  </span>
                </b-col>
                <b-col sm="6" class="text-des table-detail">
                  <span
                    ><b>Kinh độ: </b>
                    <span id="vesselType">{{
                      objectDetailDialog.longitude
                    }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="6" class="text-des table-detail">
                  <span
                    ><b>IP Nguồn: </b>
                    <span>{{ objectDetailDialog.sourceIp }}</span>
                  </span>
                </b-col>
                <b-col sm="6" class="text-des table-detail">
                  <span
                    ><b>IP Đích: </b>
                    <span id="vesselType">{{ objectDetailDialog.destIp }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="12" class="text-des table-detail">
                  <span
                    ><b>Chiều dữ liệu: </b>
                    <span>{{
                      objectDetailDialog.direction == 1
                        ? "Chiều đi"
                        : objectDetailDialog.direction == 2
                        ? "Chiều về"
                        : "không xác định"
                    }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="12" class="text-des table-detail">
                  <span
                    ><b>Cập nhật: </b>
                    <span>{{
                      objectDetailDialog.ingestTime | formatDate
                    }}</span>
                  </span>
                </b-col>
              </b-row>
              <br />
              <b-row>
                <b-col sm="2">
                </b-col>
                <b-col sm="4">
                  <el-button
                    type="primary"
                    size="small"
                    @click="openDialogAddToGroup(objectDetailDialog)"
                    >Thêm vào nhóm</el-button
                  >
                </b-col>
                <b-col sm="4">
                  <el-button
                    type="primary"
                    size="small"
                    @click="handleGetVoyageDetail(objectDetailDialog)"
                    >Xem hành trình</el-button
                  >
                </b-col>
              </b-row>
            </div>
          </b-tab>

          <b-tab title="Lịch sử hoạt động" @click="historyTabActive()">
            <div class="container-history">
              <b-row style="margin-top: 78px" :style="[objectDetailDialog.isUfo == 1?{'margin-top': '5px !important'}:{}]">
                <b-col
                  ><div class="info-history">
                    <div class="icon-info">
                      <i class="el-icon-video-play" style="font-size: 55px;"></i>
                    </div>
                    <span>Thông tin media</span><br />
                    <span
                      ><b>{{ this.totalInfoMedia }}</b></span
                    ><br />
                    <el-button
                      class="button-info"
                      :disabled="totalInfoMedia == 0"
                      @click="detailMedia()"
                      ><span>Chi tiết</span></el-button
                    >
                  </div></b-col
                >
                <b-col
                  ><div class="info-history" :style="[objectDetailDialog.isUfo == 1?{'padding-top': '22px !important'}:{}]">
                    <div class="icon-info">
                      <i class="el-icon-warning-outline" style="font-size: 55px;"></i>
                    </div>
                    <span>Thông tin sự kiện</span><br />
                    <span
                      ><b>{{ this.totalInfoEvent }}</b></span
                    ><br />
                    <el-button
                      class="button-info"
                      :disabled="totalInfoEvent == 0"
                      @click="detailEvent()"
                      ><span>Chi tiết</span></el-button
                    >
                  </div>
                </b-col>
              </b-row>
            </div>
          </b-tab>
          <b-tab title="Ghi chú"  @click="noteTabActive()">
            <div class="note-list" :style="[objectDetailDialog.isUfo == 1?{'height': '147px !important'}:{}]">
              <h6 v-for="item in listComment" :key="item.id">
                <i>
                  <small>{{ item.createdTime | formatDate }}</small></i
                >
                <br />
                <span>{{ item.content }}</span>
              </h6>
            </div>
            <b-row style="margin-right: -9px; margin-left: -5px">
              <textarea
                style="width: 77%;
                  resize:none;
                 margin: 5px; 
                 border: 1px solid #cecece"
                v-model="vesselDetailDialogNote.noteSend"
                placeholder="Nhập văn bản"
              ></textarea>
              <el-button style="width: 70px; margin: 5px" @click="addComment()">
                Lưu
              </el-button>
            </b-row>
          </b-tab>
        </b-tabs>
      </div>
    </div>

    <div class="el-button-on-map">
      <el-tooltip
        class="item"
        effect="dark"
        content="Trung tâm bản đồ"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center;"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1177.png') + ')',
          }"
          @click="centerToPoisition()"
          type="primary"
        >
        </el-button>
      </el-tooltip>
      <el-tooltip
        v-if="!isMeasureMapDraw"
        class="item"
        effect="dark"
        content="Thước đo độ dài"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1178.png') + ')',
          }"
          @click="measureMap('LineString')"
          type="primary"
        >
          <!-- <i class="el-icon-sort"></i> -->
        </el-button>
      </el-tooltip>
      <el-tooltip
        v-if="isMeasureMapDraw"
        class="item"
        effect="dark"
        content="Hủy đo độ dài"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center"
          @click="cancelMeasureMap()"
          type="primary"
        >
          <i class="el-icon-minus"></i>
        </el-button>
      </el-tooltip>
      <el-tooltip
        v-if="!isMeasureDientichMapDraw"
        class="item"
        effect="dark"
        content="Thước đo diện tích"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1179.png') + ')',
          }"
          @click="measureMap('Polygon')"
          type="primary"
        >
          <!-- <i class="el-icon-data-board"></i> -->
        </el-button>
      </el-tooltip>
      <el-tooltip
        v-if="isMeasureDientichMapDraw"
        class="item"
        effect="dark"
        content="Hủy đo diện tích"
        placement="left-start"
      >
        <el-button class="search" @click="cancelMeasureMap()" type="primary">
          <i class="el-icon-minus"></i>
        </el-button>
      </el-tooltip>
      <!-- <el-tooltip
        class="item"
        effect="dark"
        content="Clear"
        placement="left-start"
      >
        <el-button class="search" @click="clearAllFeature()" type="primary">
          <i class="el-icon-document-remove"></i>
        </el-button>
      </el-tooltip> -->
      <!-- <el-button
        class="search"
        @click="vung.isShowDialogQuery = true"
        type="primary"
      >
        <i class="el-icon-place"></i>
      </el-button> -->
      <!-- <el-button
        class="search"
        @click="handleDeleteFeature()"
        v-show="selectedFeatureID != null"
        type="primary"
      >
        <i class="el-icon-close"></i>
      </el-button>
      <el-button class="search" @click="saveDraw()" type="primary">
        <i class="el-icon-upload2"></i>
        el-icon-map-location
      </el-button>
      <el-button
        class="search"
        @click="handleEndDraw()"
        v-show="vung.isDrawing"
        type="primary"
      >
        <i class="el-icon-close"></i>
      </el-button> -->

      <!-- <el-button
        class="search"
        @click="isShowVung = true"
        v-show="!vung.isDrawing"
        type="primary"
      >
        <i class="el-icon-edit"></i>
      </el-button> -->
      <el-tooltip
        class="item"
        effect="dark"
        content="Tìm kiếm"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1181.png') + ')',
          }"
          @click="openDialogLoadListVessel()"
          type="primary"
        >
          <!-- <i class="el-icon-search"></i -->
          ></el-button
        >
      </el-tooltip>
      <el-tooltip
        class="item"
        effect="dark"
        content="Danh sách đối tượng"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1182.png') + ')',
          }"
          @click="openDialogListais()"
          type="primary"
        >
          <!-- <i class="el-icon-ship"></i -->
          ></el-button
        >
      </el-tooltip>
      <!-- <el-button
        class="search"
        @click="openDialogLoadListVesselGroup()"
        type="primary"
        ><i class="el-icon-truck"></i
      ></el-button> -->

      <!-- hien thị list table tau -->
      <!-- <el-button
        class="search"
        @click="voyage.isShowShipGrid = true"
        type="primary"
        ><i class="el-icon-ship"></i
      ></el-button> -->
    </div>
    <div id="mapOL"></div>
    <div id="mouse-position"></div>
    <el-dialog
      top="5vh"
      id="dl-list-vessel-position"
      v-dialogDrag
      class="el-bottom-search"
      title="Tìm kiếm"
      :visible.sync="isShowDialog"
      width="100%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editUserForm"
        :model="editUser"
        label-position="left"
        label-width="180px"
      >
        <!-- <b-row class="row">
          <b-col style="padding: 5px"></b-col>
          <b-col style="padding: 5px"></b-col>
          <b-col style="padding: 5px; text-align: right; margin-right: 15px"
            ><el-button
              @click="query.isAdvance = !query.isAdvance"
              type="primary"
              style="bt-search-advance"
              cl
            >
              <span>Tìm kiếm nâng cao</span>
            </el-button></b-col
          >
        </b-row> -->
        <b-row class="row">
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Từ ngày <span class="require">(*)</span></label>
            </div>
            <el-date-picker
              id="jack"
              v-model="query.startTime"
              size="small"
              format="dd/MM/yyyy HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :editable="false"
              style="width: 315px; margin-left: 10px"
              type="datetime"
              placeholder="Từ ngày"
            ></el-date-picker>
          </b-col>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Đến ngày <span class="require">(*)</span></label>
            </div>
            <el-date-picker
              v-model="query.endTime"
              size="small"
              format="dd/MM/yyyy HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :editable="false"
              style="width: 315px; margin-left: 10px"
              type="datetime"
              placeholder="Đến ngày"
            ></el-date-picker>
          </b-col>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Loại đối tượng</label>
            </div>
            <el-select
              filterable
              id="sltTypeObject"
              collapse-tags
              v-model="query.typeObject"
              clearable
              style="width: 315px; margin-left: 10px"
              placeholder="loại đối tượng"
              size="small"
            >
              <el-option
                v-for="item in lstTypeObject"
                :key="item.id"
                :label="item.value"
                :value="item.id"
              ></el-option>
            </el-select>
          </b-col>

          <b-col v-show="query.isAdvance" style="padding: 5px">
            <div class="label-form">
              <label>Quốc gia</label>
            </div>
            <el-select
              filterable
              v-model="query.country"
              value-key="id"
              placeholder="Quốc gia"
              style="width: 315px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'Quốc gia'"
                :value="null"
              ></el-option>
              <el-option
                v-for="item in listCountry"
                :key="item.id"
                :label="item.name + ' (id: ' + item.id + ')'"
                :value="item"
              ></el-option>
            </el-select>
          </b-col>

          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Vùng</label>
            </div>
            <el-select
              filterable
              v-model="query.area"
              value-key="id"
              placeholder="vùng"
              style="width: 315px; margin-left: 10px"
            >
              <el-option :key="-1" :label="'Vùng'" :value="null"></el-option>
              <el-option
                v-for="item in lstArea"
                :key="item.id"
                :label="item.name + ' (id: ' + item.id + ')'"
                :value="item"
              ></el-option>
            </el-select>
          </b-col>

          <b-col v-show="query.isAdvance" style="padding: 5px">
            <div class="label-form">
              <label>Loại tàu</label>
            </div>
            <el-select
              filterable
              v-model="query.vesselType"
              value-key="id"
              placeholder="Loại tàu"
              style="width: 315px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'Loại tàu'"
                :value="null"
              ></el-option>
              <el-option
                v-for="item in listVesselType"
                :key="item.id"
                :label="item.typeName + ' (id: ' + item.id + ')'"
                :value="item"
              ></el-option>
            </el-select>
          </b-col>
        </b-row>
        <b-row>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Nhóm tàu</label>
            </div>
            <el-select
              filterable
              v-model="query.vesselGroup"
              value-key="id"
              placeholder="Nhóm tàu"
              @change="handleChangeVesselGroupSelect"
              style="width: 315px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'Nhóm tàu'"
                :value="null"
              ></el-option>
              <el-option
                v-for="item in lstVesselGroup"
                :key="item.id"
                :label="item.name"
                :value="item"
              ></el-option>
            </el-select>
          </b-col>
        </b-row>
        <b-row v-show="query.isAdvance">
          <b-col :span="8" style="padding: 5px">
            <div class="label-form">
              <label>Nguồn thu</label>
            </div>
            <!-- multiple -->
            <el-select
              id="sltDataSource"
              filterable
              collapse-tags
              v-model="query.dataSource"
              clearable
              style="width: 315px; margin-left: 10px"
              placeholder="Nguồn thu"
              size="small"
            >
              <el-option
                v-for="item in lstDataSource"
                :key="item.id"
                :label="item.sourceName"
                :value="item.id"
              ></el-option>
            </el-select>
          </b-col>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>MMSI</label>
            </div>
            <!-- multiple -->
            <el-input
              v-model="query.mmsi"
              type="text"
              name="mmsi"
              style="width: 315px; margin-left: 10px"
              placeholder="MMSI"
            />
          </b-col>
          <!-- <b-col style="padding: 5px">
            <div class="label-form">
              <label>Số ĐT</label>
            </div>
     
            <el-input
              v-model="query.mobile"
              type="text"
              name="mobile"
              style="width: 200px; margin-left: 10px"
              placeholder="Số điện thoại"
            />
          </b-col> -->
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>IP nguồn</label>
            </div>
            <!-- multiple -->
            <el-input
              v-model="query.sourceIps"
              type="text"
              name="mobile"
              style="width: 315px; margin-left: 10px"
              placeholder="IP nguồn"
            />
          </b-col>
        </b-row>
        <b-row v-show="query.isAdvance">
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>IP Đích</label>
            </div>
            <!-- multiple -->
            <el-input
              v-model="query.destIps"
              type="text"
              name="mobile"
              style="width: 315px; margin-left: 10px"
              placeholder="IP Đích"
            />
          </b-col>

          <b-col style="padding: 5px"> </b-col>
        </b-row>
      </el-form>
      <br />
      <!-- <el-table
        id="my-table"
        v-loading="loading"
        :data="listAISShow"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column align="center" label="#" width="50">
          <template slot-scope="scope">
            <span>{{
              (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="MMSI" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.objId }}</span>
          </template>
        </el-table-column>

        <el-table-column label="IP Nguồn">
          <template slot-scope="scope">
            <span>{{ scope.row.sourceIp }}</span>
          </template>
        </el-table-column>
        <el-table-column label="IP Đích">
          <template slot-scope="scope">
            <span>{{ scope.row.destIp }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Loại đối tượng">
          <template slot-scope="scope">
            <span>{{ scope.row.isUfo == 1 ? "Đối tượng khác" : "Tàu" }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Tác động">
          <template slot-scope="scope">
            <el-button @click="aisObjectDetail(scope.row)">Chi tiết</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="query.total"
        :page.sync="query.currentPage"
        :limit.sync="query.rowsPerPage"
        :hidden="query.total < 10"
        @pagination="getListAIS"
      /> -->
      <div slot="footer" class="dialog-footer search-footer">
        <el-button type="primary" @click="handleClutersMake()"
          >Tìm kiếm</el-button
        >
        <el-button @click="isShowDialog = false">Bỏ qua</el-button>
      </div>
    </el-dialog>

    <el-dialog
      top="5vh"
      id="dl-list-vessel-position2"
      v-dialogDrag
      title="Danh sách đối tượng"
      :visible.sync="isShowListAisDialog"
      width="100%"
      :close-on-click-modal="false"
    >
      <el-table
        id="tblObjectList"
        v-loading="loading"
        :data="listAISShow"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column align="center" label="#" width="50">
          <template slot-scope="scope">
            <span>{{
              (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="MMSI" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.objId }}</span>
          </template>
        </el-table-column>

        <el-table-column label="IP Nguồn">
          <template slot-scope="scope">
            <span>{{ scope.row.sourceIp }}</span>
          </template>
        </el-table-column>
        <el-table-column label="IP Đích">
          <template slot-scope="scope">
            <span>{{ scope.row.destIp }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="ingestTime">
          <template slot-scope="scope">
            <span>{{ scope.row.ingestTime }}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="Loại đối tượng">
          <template slot-scope="scope">
            <span>{{ scope.row.isUfo == 1 ? "Đối tượng khác" : "Tàu" }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="Hướng">
          <template slot-scope="scope">
            <span>{{ scope.row.cog }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Tốc độ">
          <template slot-scope="scope">
            <span>{{ scope.row.sog }}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="Tác động">
          <template slot-scope="scope">
            <el-button @click="aisObjectDetail(scope.row)">Chi tiết</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="query.total"
        :page.sync="query.currentPage"
        :limit.sync="query.rowsPerPage"
        :hidden="query.total < 10"
        @pagination="getListAIS"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialogListais()">Bỏ qua</el-button>
      </div>
    </el-dialog>

    <!-- Dialog thông tin media của đối tượng  -->
    <el-dialog
      top="5vh"
      id="dl-media-info"
      v-dialogDrag
      :title="'Danh sách media của đối tượng: ' + this.vesselDetailDialog.objId"
      :visible.sync="isShowMediaInfoDialog"
      width="100%"
      :close-on-click-modal="false"
      @close="closeDialogPlayMedia()"
    >
      <el-row :gutter="5">
        <el-col :span="12">
          <div
            class="grid-content bg-purple"
            style="
              border: 1px solid #ffc9cd;
              max-height: 260px;
              overflow-y: auto;
              height: 250px;
            "
          >
            <!-- foreach comment media -->
            <el-row
              @click.native="toMediaClick(item)"
              v-for="item in mediaInfo.lstMedia"
              v-bind:key="item.id"
              v-bind:style="[
                mediaInfo.selectedMedia == item
                  ? { 'background-color': '#cecece' }
                  : {},
              ]"
              style="
                cursor: pointer;
                padding-left: 6px;
                border: 1px solid #ffc9cd;
                height: 50px;
              "
            >
              <span style="line-height: 48px"
                ><b>{{ item.mediaTypeName }}</b>
                {{ item.eventTime | formatDate }}</span
              >

              <el-col :span="19" class="textRight">{{ item.content }}</el-col>
            </el-row>

            <!-- <el-row style="border: 1px solid #FFC9CD;">
                      <el-col :span="5" style="font-size: 11px; background: #A3A3A3; color: white;">admin<br />17/03/2021 10:50:13</el-col>
                      <el-col :span="19" class="textRight">good</el-col>
                    </el-row>
                    <el-row style="border: 1px solid #FFC9CD;">
                      <el-col :span="5" style="font-size: 11px; background: #A3A3A3; color: white;">admin<br />17/03/2021 10:50:13</el-col>
                      <el-col :span="19" class="textRight">good</el-col>
                    </el-row> -->
          </div>
        </el-col>
        <el-col :span="12" style="">
          <div
            class="grid-content bg-purple"
            style="border: 1px solid #ffc9cd; height: 250px"
          >
            <!-- Nếu media là Web/html thì gen html ra trước, còn các loại media khác thì gen html = JS -->
            <div
              v-if="
                mediaInfo.selectedMedia &&
                mediaInfo.selectedMedia.mediaTypeId === 3
              "
            >
              <div
                class="row"
                style="
                  background-color: #f6f6f6;
                  border-radius: 2px;
                  border: 1px solid #7f7878;
                  margin-left: 0;
                  margin-right: 0;
                  height: 255px;
                "
              >
                <div style="height: 15px"></div>
                <div class="form-row" style="width: 100%">
                  <div class="col-sm-9">
                    <span style="color: red; font-weight: bold"
                      >&emsp;{{ mediaInfo.selectedMedia.fileName }}</span
                    >
                  </div>

                  <!-- <div v-if="mediaInfo.selectedMedia.filePath.endsWith('.html')" class="col-sm-1 text-right">
                    <a href="javascript:void(0)" @click="viewHtmlWeb(mediaInfo.selectedMedia.filePath)"
                        style="color: orange; font-weight: bold">Xem</a>
                  </div>
                  <div class="col-sm-2 text-left">
                    <a v-bind:href="mediaInfo.selectedMedia.filePath"
                      style="color: blue; font-weight: bold" target="_blank"
                      v-bind:download="mediaInfo.selectedMedia.filePath">Tải xuống</a>
                  </div> -->
                  <div class="col-sm-2 text-left">
                    <a
                      v-bind:href="mediaInfo.selectedMedia.filePath"
                      style="color: blue; font-weight: bold"
                      target="_blank"
                      v-bind:download="mediaInfo.selectedMedia.filePath"
                      >Xem</a
                    >
                  </div>
                </div>
              </div>
            </div>
            <!-- Hiển thị media: audio, video, .... -->
            <div ref="mediaModelVideoAudio"></div>
          </div>
        </el-col>
      </el-row>

      <!-- <el-row :gutter="5">
        <el-col :span="12">
          <div
            class="grid-content bg-purple-light"
            style="border: 1px solid #ffc9cd"
          >
            <el-row style="margin: 2px">
              <el-col :span="18" class="aliasLeft">
                <el-input
                  v-model="mediaInfo.commentValue"
                  id="addCommentInput"
                  placeholder="Nhập ghi chú ..."
                  size="small"
                ></el-input>
              </el-col>
              <el-col :span="6" class="textRight">
                <el-button
                  v-loading="loading"
                  class="search"
                  @click="addCommentMedia()"
                  type="primary"
                  size="small"
                  >Đồng ý</el-button
                >
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="12">
          <div
            class="grid-content bg-purple-light"
            style="border: 1px solid #ffc9cd"
          >
            <el-row style="margin: 2px">
  
            </el-row>
          </div>
        </el-col>
      </el-row> -->

      <el-row :gutter="5" style="margin-top: 10px">
        <el-col :span="6">
          <div class="grid-content bg-purple" style="border: 1px solid #ffc9cd">
            <el-row>
              <el-col :span="10" class="aliasLeft">ID nguồn</el-col>
              <el-col :span="14" class="textRight">{{
                mediaInfo.selectedMedia.srcId
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="10" class="aliasLeft">Loại</el-col>
              <el-col :span="14" class="textRight">{{
                mediaInfo.selectedMedia.srcIsHq === 1 ? "Bờ" : "Trên biển"
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="10" class="aliasLeft">Tên</el-col>
              <el-col :span="14" class="textRight">{{
                mediaInfo.selectedMedia.srcName
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="10" class="aliasLeft">SĐT</el-col>
              <el-col :span="14" class="textRight">{{
                mediaInfo.selectedMedia.sourcePhone
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="10" class="aliasLeft">IP</el-col>
              <el-col :span="14" class="textRight">{{
                mediaInfo.selectedMedia.sourceIp
              }}</el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="grid-content bg-purple" style="border: 1px solid #ffc9cd">
            <el-row>
              <el-col :span="6" class="aliasLeft">ID đích</el-col>
              <el-col :span="18" class="textRight">{{
                mediaInfo.selectedMedia.destId
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="6" class="aliasLeft">Loại</el-col>
              <el-col :span="18" class="textRight">{{
                mediaInfo.selectedMedia.destIsHq === 1 ? "Bờ" : "Trên biển"
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="6" class="aliasLeft">Tên</el-col>
              <el-col :span="18" class="textRight">{{
                mediaInfo.selectedMedia.destName
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="6" class="aliasLeft">SĐT</el-col>
              <el-col :span="18" class="textRight">{{
                mediaInfo.selectedMedia.destPhone
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="6" class="aliasLeft">IP</el-col>
              <el-col :span="18" class="textRight">{{
                mediaInfo.selectedMedia.destIp
              }}</el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple" style="border: 1px solid #ffc9cd">
            <el-row>
              <el-col :span="5" class="aliasLeft">Loại media</el-col>
              <el-col :span="7" class="textRight">{{
                mediaInfo.selectedMedia.mediaTypeName
              }}</el-col>
              <el-col :span="5" class="aliasLeft">Trạng thái</el-col>
              <el-col :span="7" class="textRight">{{
                mediaInfo.selectedMedia.status === 1 ? "Đã xem" : "Chưa xem"
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="5" class="aliasLeft">Thời gian</el-col>
              <el-col :span="7" class="textRight">{{
                mediaInfo.selectedMedia.eventTime | formatDate
              }}</el-col>
              <el-col :span="5" class="aliasLeft">Nguồn thu</el-col>
              <el-col :span="7" class="textRight">{{
                mediaInfo.selectedMedia.dataSourceName
              }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="5" class="aliasLeft">Dung lượng</el-col>
              <el-col :span="19" class="textRight">{{
                mediaInfo.selectedMedia.fileSize
              }}</el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeMediaInfoDialog()">Đóng</el-button>
      </div>
    </el-dialog>

    <!-- Dialog thông tin sự kiện của đối tượng  -->
    <el-dialog
      top="5vh"
      id="dl-media-info"
      v-dialogDrag
      :title="
        'Danh sách sự kiện của đối tượng: ' + this.vesselDetailDialog.objId
      "
      :visible.sync="isShowRuleEventInfoDialog"
      width="100%"
      :close-on-click-modal="false"
    >
      <el-table
        id="tbl-rule-event"
        :data="ruleEventInfo.lstRuleEvent"
        border
        fit
        highlight-current-row
        style="width: 100%; font-size: 13px"
      >
        <el-table-column align="center" label="#" width="50">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="ID" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.objId }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Luật giám sát" width="160">
          <template slot-scope="scope">
            <span
              >Cảnh báo
              {{
                scope.row.ruleActionId === 1 ? "vào vùng" : "ra khỏi vùng"
              }}</span
            >
          </template>
        </el-table-column>

        <el-table-column label="Nội dung">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Thời gian" width="140">
          <template slot-scope="scope">
            {{ scope.row.eventTime | formatDate }}
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeRuleEventInfoDialog()">Đóng</el-button>
      </div>
    </el-dialog>

    <!-- grid ship -->
    <el-dialog
      v-dialogDrag
      top="5vh"
      title="Danh sách sách vị trí tàu"
      :visible.sync="voyage.isShowShipGrid"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editUserForm"
        :model="editUser"
        label-position="left"
        label-width="180px"
      >
        <el-date-picker
          v-model="query.startTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false"
          style="width: 200px"
          type="datetime"
          placeholder="Từ ngày"
        ></el-date-picker>
        <el-date-picker
          v-model="query.endTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false"
          style="width: 200px; margin-left: 10px"
          type="datetime"
          placeholder="Đến ngày"
        ></el-date-picker>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowDialog = false">Bỏ qua</el-button>
        <el-button type="primary" @click="handleClutersMake()"
          >Tìm kiếm</el-button
        >
      </div>
    </el-dialog>

    <!-- grid nhóm tàu -->
    <el-dialog
      v-dialogDrag
      top="5vh"
      title="Danh sách sách nhóm tàu"
      :visible.sync="vesselGroup.isShowvesselGroupGrid"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editUserForm"
        :model="editUser"
        label-position="left"
        label-width="180px"
      >
        <el-date-picker
          v-model="vesselGroupQuery.startTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false"
          style="width: 200px"
          type="datetime"
          placeholder="Từ ngày"
        ></el-date-picker>
        <el-date-picker
          v-model="vesselGroupQuery.endTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false"
          style="width: 200px; margin-left: 10px"
          type="datetime"
          placeholder="Đến ngày"
        ></el-date-picker>
      </el-form>
      <br />
      <el-table
        id="tblGroupVessel"
        v-loading="loading"
        :data="lstVesselGroup"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column align="center" label="#" width="50">
          <template slot-scope="scope">
            <span>{{
              (vesselGroupQuery.currentPage - 1) *
                vesselGroupQuery.rowsPerPage +
              scope.$index +
              1
            }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="ID" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.id }}</span>
          </template>
        </el-table-column> -->

        <el-table-column label="Tên nhóm tàu">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Ghi chú">
          <template slot-scope="scope">
            <span>{{ scope.row.note }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Tác động" width="150">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleGetVoyageDetail(scope)"
              >Chi tiết hành trình</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="vesselGroupQuery.total"
        :page.sync="vesselGroupQuery.currentPage"
        :limit.sync="vesselGroupQuery.rowsPerPage"
        :hidden="false"
        @pagination="getListVesselGroup"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowDialog = false">Bỏ qua</el-button>
        <el-button type="primary" @click="handleGetVesselGroup()"
          >Tìm kiếm</el-button
        >
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      top="5vh"
      title="Tìm kiếm danh sách vùng"
      :visible.sync="vung.isShowDialogQuery"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="getListAreaForm"
        :model="editUser"
        label-position="left"
        label-width="180px"
      >
        <el-date-picker
          v-model="areaQuery.startTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          :editable="false"
          style="width: 200px"
          type="datetime"
          placeholder="Từ ngày"
        ></el-date-picker>
        <el-date-picker
          v-model="areaQuery.endTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          :editable="false"
          style="width: 200px; margin-left: 10px"
          type="datetime"
          placeholder="Đến ngày"
        ></el-date-picker>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vung.isShowDialogQuery = false">Bỏ qua</el-button>
        <el-button type="primary" @click="handGetListAreaDraw()"
          >Tìm kiếm</el-button
        >
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      top="5vh"
      title="Vùng"
      :visible.sync="isShowVung"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addAreaForm"
        :model="vung"
        label-position="left"
        label-width="150px"
      >
        <el-form-item prop="areaName" label="Tên vùng">
          <el-input
            v-model="vung.name"
            name="areaName"
            type="text"
            auto-complete="on"
            placeholder="Tên vùng"
          />
        </el-form-item>
        <el-form-item prop="areaType" label="Loại vùng">
          <el-select id="type" v-model="vung.vungType">
            <el-option value="Point"></el-option>
            <el-option value="LineString">LineString</el-option>
            <el-option value="Polygon">Polygon</el-option>
            <!-- <el-option value="Circle">Circle</el-option> -->
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowVung = false">Bỏ qua</el-button>
        <el-button type="primary" @click="handleDrawVung()">Tạo vùng</el-button>
      </div>
    </el-dialog>
    <!-- thêm vào nhóm -->
    <el-dialog
      v-dialogDrag
      id="dialog-addToGroup"
      top="5vh"
      title="Thêm vào nhóm"
      :visible.sync="addToGroup.isShowDialogAddToGroup"
      width="100%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addToGroup"
        :model="addToGroup"
        label-position="left"
        style="margin-left: 10px; margin-right: 10px"
        label-width="0px"
      >
        <el-form-item prop="nhóm">
          <b-row>
            <b-col>
              <input
                type="radio"
                id="nhomcosan"
                value="1"
                v-model="addToGroup.data.typeGroup"
              />
              <label for="nhomcosan">Nhóm có sẵn</label>
            </b-col>
            <b-col>
              <input
                type="radio"
                id="nhommoi"
                value="2"
                v-model="addToGroup.data.typeGroup"
              />
              <label for="nhommoi">Nhóm mới</label>
            </b-col>
          </b-row>
        </el-form-item>
        <!-- chọn nhóm -->
        <el-form-item prop="chọn nhóm" v-if="addToGroup.data.typeGroup == 1">
          <b-row>
            <el-select
              v-if="renderAddToGroupSelect"
              v-model="addToGroup.data.group"
              value-key="id"
              placeholder="Nhóm có sẵn"
              @change="handleChangeAddToGroupSelect"
              style="width: 200px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'Nhóm có sẵn'"
                :value="null"
              ></el-option>
              <el-option
                v-for="item in lstVesselGroup"
                :key="item.id"
                :label="item.name"
                :value="item"
              ></el-option>
            </el-select>
          </b-row>
        </el-form-item>
        <!-- tạo nhóm mới -->
        <el-form-item prop="chọn nhóm" v-if="addToGroup.data.typeGroup == 2">
          <b-row>
            <input
              class="form-control"
              v-model="addToGroup.data.newGroup"
              placeholder="Nhập nhóm mới"
            />
          </b-row>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialogAddToGroup()">Bỏ qua</el-button>
        <el-button type="primary" @click="toAddToGroup()">Thêm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Map from "ol/Map.js";
import View from "ol/View.js";
import router from "../../../router";
import ImageWMS from "ol/source/ImageWMS";
import { OSM, Vector as VectorSource } from "ol/source";
import { fromLonLat, toLonLat } from "ol/proj";
import VectorLayer from "ol/layer/Vector";
import { Image as ImageLayer } from "ol/layer";
import { Tile as TileLayer, Vector } from "ol/layer";
import MousePosition from "ol/control/MousePosition";
import { createStringXY } from "ol/coordinate";
import { GeoJSON, WKT } from "ol/format";
import XYZ from "ol/source/XYZ";
import Point from "ol/geom/Point";
import TileWMS from "ol/source/TileWMS";
import { Draw, Modify, Snap, defaults, Translate } from "ol/interaction";
import { LineString, Polygon } from "ol/geom";
import Feature from "ol/Feature";
import Circle from "ol/geom/Circle";
import { Cluster, Stamen } from "ol/source";
import OverallApi from "../api/overall/api";
import { toStringHDMS } from "ol/coordinate";
import { defaults as defaultControls } from "ol/control";
import $ from "jquery";
import Overlay from "ol/Overlay";
var host = process.env.VUE_APP_BASE_API;
const dataSourceResource = new Resource("manager/source/get-all");
import axios from "axios";
import { getArea, getLength } from "ol/sphere";
import { unByKey } from "ol/Observable";
import {
  Circle as CircleStyle,
  Icon,
  Style,
  Stroke,
  RegularShape,
  Fill,
  Text,
} from "ol/style";
import { Select, defaults as defaultInteractions } from "ol/interaction";
import KML from "ol/format/KML";
import { createEmpty, extend, getHeight, getWidth } from "ol/extent";
import Pagination from "@/components/Pagination";
import Resource from "@/api/resource";
const countriesResource = new Resource("ais/country");
const vesselTypeResource = new Resource("ais/vessel-type");
const hanoi = [105.804817, 21.028511];
let map;
var source = new VectorSource({ wrapX: false });
let currentDraw;

let today = new Date();
let day = checkZero(today.getDate() + "");
let month = checkZero(today.getMonth() + 1 + "");

var stroke = new Stroke({ color: "#cecece", width: 0.4 });
var fill = new Fill({ color: 'rgba(0, 255, 0, 0.4)' });

const hanoiWebMercator = fromLonLat(hanoi);

var currentDrawSource = new VectorSource({ wrapX: false });

var earthquakeFill = new Fill({
  color: "rgba(255, 153, 0, 0.8)",
});
var earthquakeStroke = new Stroke({
  color: "rgba(255, 204, 0, 0.2)",
  width: 1,
});

var textFill = new Fill({
  color: "#fff",
});
var textStroke = new Stroke({
  color: "rgba(0, 0, 0, 0.6)",
  width: 3,
});
var invisibleFill = new Fill({
  color: "rgba(255, 255, 255, 0.01)",
});

var mousePositionControl = new MousePosition({
  coordinateFormat: createStringXY(4),
  projection: "EPSG:4326",
  // comment the following two lines to have the mouse position
  // be placed within the map.
  className: "custom-mouse-position",
  target: document.getElementById("mouse-position"),
  undefinedHTML: "&nbsp;",
});

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

function selectStyleFunction(feature) {
  if (currentDraw != null) {
    return;
  }
  var styles = [
    new Style({
      image: new CircleStyle({
        radius: feature.get("radius"),
        fill: new Fill({
          color: "rgba(255, 255, 255, 0.01)",
        }),
      }),
    }),
  ];
  var originalFeatures = feature.get("features");
  var originalFeature;
  for (var i = originalFeatures.length - 1; i >= 0; --i) {
    originalFeature = originalFeatures[i];
    styles.push(createEarthquakeStyle(originalFeature));
  }
  return styles;
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
  // geometry.forEachSegment(function (start, end) {
  //   var dx = end[0] - start[0];
  //   var dy = end[1] - start[1];
  //   debugger;
  //   var rotation = Math.atan2(dy, dx);
  //   // arrows
  //   styles.push(
  //     new Style({
  //       geometry: new Point(end),
  //       image: new Icon({
  //         src:
  //           "http://192.168.61.106:9985/vsat@/assets/icon/object/Yacht.png",
  //         anchor: [0.5, 0.5],

  //         scale: 1.2,
  //         rotateWithView: true,
  //         rotation: -rotation-180,
  //       }),
  //     })
  //   );
  //   styles.push(
  //     new Style({
  //       geometry: new Point(start),
  //       image: new Icon({
  //         src:
  //           "http://192.168.61.106:9985/vsat@/assets/icon/object/Yacht.png",
  //         anchor: [0.5, 0.5],

  //         scale: 1.2,
  //         rotateWithView: true,
  //         rotation: -rotation-180,
  //       }),
  //     })
  //   );
  // });

  return styles;
};

function getObjectIcon(data,isMaster,statusId){

  var result = require('@/assets/icon/object/Yacht.png');
  if(data&&data.typeId){
    var vesselTypeId=data.typeId;
    
    if (isMaster == 1) {
        result =  require("@/assets/icon/object/Master.png");
    } else if (statusId == 1 || statusId == 5) {
        result = require("@/assets/icon/object/AnchorMoored.png");
    } else {
      console.log("getObjectIcon: ", vesselTypeId)
        if (vesselTypeId >= 40 && vesselTypeId <= 49) {
            result =require("@/assets/icon/object/HightSpeedCraft.png");
        } else if ((vesselTypeId >= 50 && vesselTypeId <= 59) || vesselTypeId == 10) {
            result = require("@/assets/icon/object/TugPilot.png");
        } else if (vesselTypeId >= 60 && vesselTypeId <= 69) {
            result = require("@/assets/icon/object/Passenger.png");
        } else if (vesselTypeId >= 70 && vesselTypeId <= 79) {
            result = require("@/assets/icon/object/Cargo.png");
        } else if (vesselTypeId >= 80 && vesselTypeId <= 89) {
            result = require("@/assets/icon/object/Tanker.png");
        } else {
            result = require("@/assets/icon/object/Yacht.png");
        }
    }
  }
  return result;
}

var iconVessel;

function createEarthquakeStyle(feature) {
  // 2012_Earthquakes_Mag5.kml stores the magnitude of each earthquake in a
  // standards-violating <magnitude> tag in each Placemark.  We extract it
  // from the Placemark's name instead.
  // var name = feature.get("name");
  // var magnitude = parseFloat(name.substr(2));
  // var radius = 5 + 20 * (magnitude - 5);
  // console.log("direction: ", feature.values_.data.sog);
  iconVessel = new Icon({
    src: getObjectIcon(feature.values_.data), // "http://192.168.61.106:9985/vsat@/assets/icon/object/Yacht.png",
    anchor: [0.5, 0.5],
    scale: 1.7,
    rotateWithView: true,
    rotation: (feature.values_.data.cog * Math.PI) / 180,
  });

  return new Style({
    geometry: feature.getGeometry(),
    image: iconVessel,
    text: new Text({
      text: feature.values_.data ? feature.values_.data.objId : "",
      stroke: new Stroke({
        color: "black",
        width: 2,
      }),
      fill: new Fill({
        color: "#fff",
      }),
    }),
  });
}

var maxFeatureCount;
var vector = null;
var calculateClusterInfo = function (resolution) {
  maxFeatureCount = 0;
  var features = vector.getSource().getFeatures();
  var feature, radius;
  for (var i = features.length - 1; i >= 0; --i) {
    feature = features[i];
    var originalFeatures = feature.get("features");
    var extent = createEmpty();
    var j = void 0,
      jj = void 0;
    for (j = 0, jj = originalFeatures.length; j < jj; ++j) {
      extend(extent, originalFeatures[j].getGeometry().getExtent());
    }
    maxFeatureCount = Math.max(maxFeatureCount, jj);
    radius = (0.25 * (getWidth(extent) + getHeight(extent))) / resolution;
    feature.set("radius", radius);
  }
};

var currentResolution;
function styleFunctionClutter(feature, resolution) {
  if (resolution != currentResolution) {
    console.log("resolution: ",resolution);
    calculateClusterInfo(resolution);
    currentResolution = resolution;
  }
  var style;
  var size = feature.get("features").length;
  if (size > 1) {
    style = new Style({
      image: new RegularShape({
        fill: size > 1000 ? fill : fill,
        stroke: stroke,
        points: 4,
        radius: 40,
        angle: Math.PI / 4,
        scale: [2, 1],
      }),
      // image: new CircleStyle({
      //   radius: feature.get("radius"),
      //   fill: new Fill({
      //     color: "#06b906", //[255, 153, 0, Math.min(0.8, 0.4 + size / maxFeatureCount)]
      //   }),
      // }),
      text: new Text({
        text: size.toString(),
        fill: textFill,
        stroke: textStroke,
      }),
    });
  } else {
    var originalFeature = feature.get("features")[0];
    style = createEarthquakeStyle(originalFeature);

  }
  return style;
}

function convertCoordinates(lon, lat) {
  return [lon, lat];

  // var x = (lon * 20037508.34) / 180;
  // var y = Math.log(Math.tan(((90 + lat) * Math.PI) / 360)) / (Math.PI / 180);
  // y = (y * 20037508.34) / 180;
  // return [x, y];
}

var sketch;

/**
 * The help tooltip element.
 * @type {HTMLElement}
 */
var helpTooltipElement;

/**
 * Overlay to show the help messages.
 * @type {Overlay}
 */
var helpTooltip;

/**
 * The measure tooltip element.
 * @type {HTMLElement}
 */
var measureTooltipElement;

/**
 * Overlay to show the measurement.
 * @type {Overlay}
 */
var measureTooltip;

var listener;
/**
 * Message to show when the user is drawing a polygon.
 * @type {string}
 */
var continuePolygonMsg = "Click to continue drawing the polygon";

/**
 * Message to show when the user is drawing a line.
 * @type {string}
 */
var continueLineMsg = "Click to continue drawing the line";

var overLayPopup = null;
var feature_onClick;

var draw;

var sourceMeasure = new VectorSource({ wrapX: false });

var vectorMeasure = new VectorLayer({
  isMeasure: true,
  source: sourceMeasure,
  style: new Style({
    fill: new Fill({
      color: "#748fc5a8",
    }),
    stroke: new Stroke({
      color: "#ffcc33",
      width: 2,
    }),
    image: new CircleStyle({
      radius: 7,
      fill: new Fill({
        color: "#ffcc33",
      }),
    }),
  }),
});

export default {
  name: "map-openlayers",
  components: {
    Pagination,
  },
  data() {
    return {
      isMeasureDientichMapDraw: false,
      isMeasureMapDraw: false,
      isShowListAisDialog: false,
      stepTabDetail: 0,
      mediaInfo: {
        lstMedia: [],
        comments: [],
        mediaDetail: {},
        commentValue: "",
        query: {
          currentPage: 1,
          rowsPerPage: 30000,
        },
        selectedMedia: {},
      },
      isShowMediaInfoDialog: false,
      ruleEventInfo: {
        lstRuleEvent: [],
        ruleEventTotal: 0,
        query: {
          currentPage: 1,
          rowsPerPage: 10,
        },
      },
      isShowRuleEventInfoDialog: false,
      totalInfoMedia: 0,
      totalInfoEvent: 0,
      lstTypeObject: [
        {
          id: 0,
          value: "Tàu",
        },
        {
          id: 1,
          value: "Đối tượng khác",
        },
      ],
      listComment: null,
      renderAddToGroupSelect: true,
      addToGroup: {
        isShowDialogAddToGroup: false,
        data: {
          typeGroup: 1,
          group: null,
        },
        listVesselGroup: null,
      },
      translate: null,
      positionInfo: {},
      listAIS: [],
      listAISShow: [],
      loading: false,
      voyage: {
        isShowShipGrid: false,
      },
      vesselGroup: {
        isShowvesselGroupGrid: false,
      },
      lstVesselFeature: [],
      areaQuery: {
        startTime: null,
        endTime: null,
        perPage: 5,
        currentPage: 1,
        rowsPerPage: 5,
        vesselGroup: "",
        total: 0,
        sort: "createdAt",
      },
      listCountry: [],
      listCountrySearch: [
        {
          id: null,
          name: null,
        },
      ],
      listVesselType: [],
      list_vesselTypeSearch: [
        {
          id: null,
          name: null,
        },
      ],
      query: {
        isAdvance: true,
        sourceIps: null,
        destIps: null,
        mmsi: null,
        mobile: null,
        startTime: today.getFullYear() + "-" + month + "-" + day + " 00:00:00",
        endTime: getCurrentDateToStr(today),
        perPage: 5,
        currentPage: 1,
        rowsPerPage: 5,
        vesselGroup: "",
        area: null,
        vesselType: null,
        country: null,
        total: 0,
        sort: "createdAt",
        dataSource: [],
        typeObject: 0,
      },
      lstDataSource: [],
      lstVesselGroup: [],
      lstArea: [],
      vesselGroupQuery: {
        startTime: null,
        endTime: null,
        currentPage: 1,
        rowsPerPage: 5,
        total: 0,
        sort: "createdAt",
      },
      featureID: 0,
      singleClick: null,
      selectedFeatureID: null,

      isShowDialog: false,
      isShowVung: false,
      editUser: {},
      vung: {
        isShowDialogQuery: false,
        vungType: "Polygon",
        isDrawing: false,
        name: "",
      },
      objectDetailDialog: {},
      vesselDetailDialog: {},
      vesselDetailDialogNote: {
        noteSend: "",
      },
      isShowVesselDetailDialog: true,
    };
  },
  created() {
    console.log("Created");
  },
  mounted() {
    console.log("param query: ", this.$route.query);
    source = new VectorSource({ wrapX: false });

    var vector = new VectorLayer({
      source: source,
    });
    console.log("mounted", map);
    if (map != null) {
      map = null;
    }

    this.$nextTick(function () {
      setTimeout(() => {
        this.initiateMap();
      }, 1000);
    });

    this.init();
  },
  methods: {
    init() {
      this.handleGetVesselGroup(1, 10000000);
      this.handleGetListArea();
      this.getListVesselType();
      this.getListCountries();
      this.getAllDataSource();
    },
    initiateMap() {
      console.log("init map... ", map);

      var source = new VectorSource();
      // var vector = new VectorLayer({
      //   source: source,
      // });
      // create title layer
      var raster = new TileLayer({
        source: new OSM(),
      });

      var vector = new VectorLayer({
        source: currentDrawSource,
      });

      console.log("new map... ", map);

      // create map with 2 layer
      map = new Map({
        target: "mapOL",
        controls: defaultControls().extend([mousePositionControl]),
        // interactions: defaultInteractions().extend([
        //   new Select({
        //     condition: function (evt) {
        //       return evt.type == "pointermove" || evt.type == "singleclick";
        //     },
        //     style: selectStyleFunction,
        //   }),
        // ]),
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
          vectorMeasure,
        ],
        view: new View({
          projection: "EPSG:4326",
          center: hanoi,
          zoom: 4,
        }),
      });
      overLayPopup = new Overlay({
        element: document.getElementById("popup"),
        offset: [450, 0],
      });

      map.on("click", this.mapClick.bind(this));

      map.addOverlay(overLayPopup);
    },

    async mapClick(evt) {
      if (currentDraw != null) {
        return;
      }
      if (draw) {
        return;
      }

      var element = overLayPopup.getElement();
      var coordinate = evt.coordinate ? evt.coordinate : evt;
      var hdms = toStringHDMS(toLonLat(coordinate));
      console.log("map click .pixel ", map.getPixelFromCoordinate(coordinate));
      overLayPopup.setPosition(coordinate);
      if (evt.pixel) {
        feature_onClick = map.forEachFeatureAtPixel(
          evt.pixel,
          function (feature, layer) {
            console.log(feature);
            return feature;
          }
        );
      } else {
        feature_onClick = map.forEachFeatureAtPixel(
          map.getPixelFromCoordinate(coordinate),
          (feature, layer) => {
            console.log("forEachFeature ", feature);
            return feature;
          }
        );
      }

      if (feature_onClick) {
        // var content = document.getElementById("popup-content");
        // console.log(feature_onClick.getProperties().name);
        // overLayPopup.setPosition(evt.coordinate);
        this.isShowVesselDetailDialog = true;

        if (
          feature_onClick.values_.features &&
          feature_onClick.values_.features.length == 1
        ) {
          var data = feature_onClick.values_.features[0].values_.data;
          console.log(data);
          this.centerToPoisition(
            coordinate[0],
            coordinate[1],
            map.getView().getZoom()
          );
          const overallApi = new OverallApi();
          var res = {};
          this.stepTabDetail = 0;
          if (data.isUfo == 1) {
            try {
              if (data&&typeof(data.objId) === "number") {
                return;
              }
              this.vesselDetailDialog = {};
              this.objectDetailDialog = { ...data, isUfo: 1 };
              var res = await overallApi.postApi(
                "/contact/Object/detail/getDetailObjectInfo",
                {
                  uuid: data.objId,
                }
              );
              if (res.status == 200) {
                var objectDetailDialog =
                  res.data != null ? res.data.objectUnInfoDto : {};

                this.objectDetailDialog = objectDetailDialog;
              }
              this.objectDetailDialog = { ...data, ...this.objectDetailDialog };
              console.log("detail un object ", this.objectDetailDialog);
              this.stepTabDetail = 0;
            } catch (error) {
              this.$notify({
                title: "Lấy thông tin đối tượng thất bại.",
                type: "error",
              });
              return;
            }
          } else if (data.isUfo == 0) {
            try {
              this.vesselDetailDialog = { isUfo: 1 };
              this.objectDetailDialog = {};
              var res = await overallApi.getVesselDetail(
                "/ais/vessel/detail/" + data.objId
              );
              if (res.status == 200) {
                var vesselDetailInfo = res.data != null ? res.data : {};

                this.vesselDetailDialog = vesselDetailInfo;
              }
              this.vesselDetailDialog = { ...data, ...this.vesselDetailDialog };
              console.log("detail vessel ", this.vesselDetailDialog);
            } catch (error) {
              this.$notify({
                title: "Lấy thông tin tàu thất bại.",

                type: "error",
              });
              return;
            }
          }
        } else if (feature_onClick.values_.data) {
          var data = feature_onClick.values_.data;
          console.log(data);
          const overallApi = new OverallApi();
          var res = await overallApi.getVesselDetail(
            "/ais/vessel/detail/" + data.objId
          );
          if (res.status == 200) {
            var vesselDetailInfo = res.data != null ? res.data : {};
            console.log("detail vessel ", vesselDetailInfo);
            this.vesselDetailDialog = vesselDetailInfo;
          }
          this.vesselDetailDialog = { ...data, ...this.vesselDetailDialog };
          console.log("detail vessel ", this.vesselDetailDialog);
        } else if (feature_onClick.type == "area") {
          var data = feature_onClick.data;
          console.log(data);
          document.getElementById("popup-content").innerHTML =
            "vùng: " + data.name + "</br>";
        } else {
          this.centerToPoisition(
            coordinate[0],
            coordinate[1],
            map.getView().getZoom() + 1
          );
          overLayPopup.setPosition(null);
          this.isShowVesselDetailDialog = false;
          this.positionInfo.longitude = coordinate[0];
          this.positionInfo.latitude = coordinate[1];
        }
      } else {
        overLayPopup.setPosition(null);
        this.isShowVesselDetailDialog = false;
        this.positionInfo.longitude = coordinate[0];
        this.positionInfo.latitude = coordinate[1];
      }
    },

    handleChangeAddToGroupSelect() {
      // Remove my-component from the DOM
      debugger;
      this.renderAddToGroupSelect = false;

      this.$nextTick(() => {
        // Add the component back in
        this.renderAddToGroupSelect = true;
      });
    },

    setAltImg(event) {
      // console.log("image error");
      event.target.src = require("@/assets/images/vessel/vessel_default.jpg");
    },

    closeDialogAddToGroup() {
      this.addToGroup.isShowDialogAddToGroup = false;
    },

    async openDialogAddToGroup() {
      this.handleGetVesselGroup(1, 10000000);
      this.addToGroup.isShowDialogAddToGroup = true;
      this.addToGroup.data.group = null;
      this.addToGroup.data.newGroup = null;
    },

    async getAllDataSource() {
      try {
        var res = await dataSourceResource.list();
        if (res.status == 200) {
          this.lstDataSource = res.data;
        }
        console.log("data source ", res);
      } catch (error) {
        console.error("data source ", error);
      }

      // if (data) this.dataSourceLst = data;
    },

    async getListVesselType() {
      vesselTypeResource
        .list()
        .then((response) => {
          if (response.status === 200) {
            this.listVesselType = response.data;
            this.list_vesselTypeSearch = response.data;
            this.vesselTypeInfo = {
              id: response.data[0].id,
              name: response.data[0].typeName,
            };
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async getListCountries() {
      countriesResource
        .list()
        .then((response) => {
          if (response.status === 200) {
            this.listCountry = response.data;
            this.listCountrySearch = response.data;
            this.list_placeOfBuild = response.data;

            this.countryInfo = {
              id: response.data[0].id,
              name: response.data[0].name,
            };

            this.placeOfBuildInfo = {
              id: response.data[0].id,
              name: response.data[0].name,
            };
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    async handleGetVoyageDetail(data) {
      console.log("handleGetVoyageDetail", data);
      // this.$router.push({
      //   path: "/map/voyage",
      //   query: {
      //     objId: data.row ? data.row.objId : data.objId,
      //     isUfo: data.row ? data.row.isUfo : data.isUfo,
      //   },
      // });

      let routeData = this.$router.resolve({path: "/map/voyage", 
      query: {
          objId: data.row ? data.row.objId : data.objId,
          isUfo: data.row ? data.row.isUfo : data.isUfo,
          startTime:this.query.startTime,
          endTime:this.query.endTime,
        }
      });
      window.open(routeData.href, '_blank');

      return;
      this.clearAllFeature();
      console.log("GetVoyageDetail", data);
      const overallApi = new OverallApi();
      var resListAISDetail = await overallApi.searchListAis(
        "/ais/search-list",
        {
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          mmsi: data.row ? data.row.objId : data.objId,
          limit: 10000,
        }
      );

      console.log("result VoyageDetail", resListAISDetail);
      if (resListAISDetail.status == 200) {
        console.log("result VoyageDetail", resListAISDetail.data);
        if (resListAISDetail.data && resListAISDetail.data.length < 2) {
          this.$notify({
            title: "Chỉ có 1 ví trí tàu được tìm thấy",
            position: "top right",
            type: "waring",
          });
        }
        this.handleLineStringDraw(resListAISDetail.data);
      }
      if (resListAISDetail.data && resListAISDetail.data.length > 0) {
        this.centerToPoisition(
          resListAISDetail.data[0].longitude,
          resListAISDetail.data[0].latitude
        );
      }

      this.isShowDialog = false;
    },

    closeDialogSearchAisvessel() {
      this.isShowDialog = false;
    },

    openMediaInfoDialog() {
      this.isShowMediaInfoDialog = true;
      this.mediaInfo.query.currentPage = 1;
      this.mediaInfo.query.rowsPerPage = 30;
    },
    closeMediaInfoDialog() {
      this.isShowMediaInfoDialog = false;
    },

    openRuleEventInfoDialog() {
      this.isShowRuleEventInfoDialog = true;
      this.ruleEventInfo.query.currentPage = 1;
      this.ruleEventInfo.query.rowsPerPage = 30;
    },
    closeRuleEventInfoDialog() {
      this.isShowRuleEventInfoDialog = false;
    },

    viewHtmlWeb(filePath) {
      console.warn("filePath: " + filePath);
      this.dialogMediaDetailHtmlWebVisible = true;
    },

    mediaListBuildVideoAudioByMediaId(filePath, mediaTypeId) {
      let html = "";
      if (mediaTypeId === 1)
        // audio
        html =
          '<video id="mediaPlayerId" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
          '    <source src="' +
          filePath +
          '" type="audio/wav">' +
          "</video>";
      else if (mediaTypeId === 2) {
        // video
        if (filePath.endsWith(".ts"))
          // video stream
          html =
            '<video id="mediaPlayerId" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
            "</video>";
        // mp4, etc,....
        else
          html =
            '<video id="mediaPlayerId" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
            '    <source src="' +
            filePath +
            '" type="video/mp4">' +
            "</video>";
      } else if (mediaTypeId === 3) {
        // Web
        // Không gen ra html vì đã gen html sẵn rồi ( <div v-if="mediaDetail.mediaTypeId === 3"> ), case này code ra nhìn cho rõ ràng chứ ko có case này xảy ra được, do đã check mediaTypeId <> 3 từ trước khi vào hàm này.
      } else {
        // Email || transfer file
        html =
          '<div class="row" style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878; margin-left: 0; margin-right: 0; height: 255px">' +
          '     <div style="height: 15px"></div>' +
          '     <div class="form-row" style="width: 100%">' +
          '         <div class="col-sm-9">' +
          '             <span style="color: red; font-weight: bold">&emsp;' +
          filePath.substring(filePath.lastIndexOf("/") + 1) +
          "</span>" +
          "         </div>";
        if (mediaTypeId !== 8 && filePath.endsWith(".html")) {
          html +=
            '         <div class="col-sm-1 text-right">' +
            '             <a href="' +
            filePath +
            '" style="color: orange; font-weight: bold" target="_blank">Xem</a>' +
            "         </div>";
        }
        html +=
          '				<div class="col-sm-2 text-left">' +
          '             <a href="' +
          filePath +
          '" style="color: blue; font-weight: bold" target="_blank">Tải xuống</a>' +
          "         </div>" +
          "     </div> " +
          " </div>";
      }
      return html;
    },

    toMediaClick(data) {
      console.log("toMediaClick", data);
      this.mediaInfo.selectedMedia = data;

      this.mediaInfo.selectedMedia.fileSize = commonFormatBytesToSize(
        this.mediaInfo.selectedMedia.fileSize
      );

      if (
        this.mediaInfo.selectedMedia &&
        this.mediaInfo.selectedMedia.mediaTypeId === 3
      ) {
        // Xóa phần hiển thị cũ như Audio, video, .... nếu trước đó đã từng gen ra content.
        const mediaElement = this.$refs.mediaModelVideoAudio;
        if (mediaElement) mediaElement.innerHTML = "";

        //Return, ko xử lý gen html từ JS nữa.
        return;
      }

      let htmlVideoAudio = this.mediaListBuildVideoAudioByMediaId(
        this.mediaInfo.selectedMedia.filePath,
        this.mediaInfo.selectedMedia.mediaTypeId
      );
      this.$nextTick(() => {
        const mediaElement = this.$refs.mediaModelVideoAudio;
        if (mediaElement) mediaElement.innerHTML = htmlVideoAudio;

        if (
          this.mediaInfo.selectedMedia.filePath.endsWith(".ts") &&
          this.mediaInfo.selectedMedia.filePathLocal
        ) {
          console.log(
            "filePathLocal: " + this.mediaInfo.selectedMedia.filePathLocal
          );
          let payLoad = {
            filePathLocal: this.mediaInfo.selectedMedia.filePathLocal,
          };

          axios
            .post(`${host}/media/fetch-m3u8-file`, payLoad)
            // .post(`http://192.168.61.106:8415/v1.0/media/fetch-m3u8-file`, payLoad)
            .then((res) => {
              //console.warn('<== fetch-m3u8-file return: ' + JSON.stringify(res));
              if (res.data && res.data.data) {
                let m3u8Resource = res.data.data;
                var videoData = document.getElementById("mediaPlayerId");
                if (Hls.isSupported()) {
                  var hls = new Hls();
                  hls.loadSource(m3u8Resource);
                  hls.attachMedia(videoData);
                  hls.on(Hls.Events.MANIFEST_PARSED, function () {
                    videoData[0].play();
                  });
                }
              }
            })
            .catch((e) => {
              console.error(e);
            });
        }
      });
    },

    aisObjectDetail(data) {
      console.log("aisObjectDetail: ", data);
      this.closeDialogListais();
      this.centerToPoisition(data.longitude, data.latitude, 25);
      this.$nextTick(function () {
        setTimeout(() => {
          this.mapClick([data.longitude, data.latitude]);
        }, 200);
      });
    },

    noteTabActive() {
      this.loadListComment();
    },

    async loadListComment() {
      const overallApi = new OverallApi();
      var res = await overallApi.getListComment(
        "/media/list-comments",
        {
          refId: this.vesselDetailDialog.objId,
          commentTypeId: 3,
        }
        // this.vesselDetailDialog.objId
      );
      console.log("loadListComment ", res);
      if (res.status == 200) {
        this.listComment = res.data != null ? res.data : [];
      } else {
        this.listComment = [];
      }
    },

    async addComment() {
      if (
        !this.vesselDetailDialogNote.noteSend ||
        this.vesselDetailDialogNote.noteSend == null ||
        this.vesselDetailDialogNote.noteSend == ""
      ) {
        return;
      }
      const overallApi = new OverallApi();
      var res = await overallApi.getListComment(
        "/media/list/addCommentCommon",
        {
          commentTypeId: 3,
          refId: this.vesselDetailDialog.objId,
          content: this.vesselDetailDialogNote.noteSend,
          createUser: "admin",
        }
        // this.vesselDetailDialog.objId
      );
      console.log("loadListComment ", res);
      if (res.status == 200) {
        this.vesselDetailDialogNote.noteSend = [];
        this.loadListComment();
      } else {
        this.listComment = [];
      }
    },

    async toAddToGroup(data) {
      if (this.addToGroup.data.typeGroup == 1) {
        console.log("addToGroup", this.vesselDetailDialog);
        if (this.addToGroup.data.group == null) {
          this.$notify({
            title: "Nhóm không được để trống",

            type: "error",
          });
          return;
        }
        const overallApi = new OverallApi();
        var resListAISDetail = await overallApi.searchListAis(
          "/ais/object/add-to-group",
          {
            objId: "75faa863-8da5-4887-ae32-1c2482e41bf4", //this.vesselDetailDialog.objId?this.vesselDetailDialog.objId:this.objectDetailDialog.objId,
            objectGroupId: this.addToGroup.data.group.id,
          }
        );

        console.log("result VoyageDetail", resListAISDetail);
        if (resListAISDetail.status == 200) {
          console.log("result VoyageDetail", resListAISDetail.data);
          this.$notify({
            title: "Thêm tàu vào nhóm thành công.",

            type: "success",
          });
          this.closeDialogAddToGroup();
        }
      } else if (this.addToGroup.data.typeGroup == 2) {
        console.log("addToGroup", this.addToGroup);
        const overallApi = new OverallApi();
        var resListAISDetail = await overallApi.searchListAis(
          "/ais/object/add-to-group",
          {
            objId: this.vesselDetailDialog.objId
              ? this.vesselDetailDialog.objId
              : this.objectDetailDialog.objId,
            groupName: this.addToGroup.data.newGroup,
            type: 2,
            groupType: 1,
          }
        );

        console.log("result VoyageDetail", resListAISDetail);
        if (resListAISDetail.status == 200) {
          console.log("result VoyageDetail", resListAISDetail.data);
          this.$notify({
            title: "Thêm đối tượng vào nhóm thành công.",

            type: "success",
          });
          this.closeDialogAddToGroup();
        }
      }
    },

    handleChangeVesselGroupSelect() {
      console.log("vessel group change");
    },

    //phân trang list nhóm tàu
    getListVesselGroup(obj) {
      console.log("opaging: ", obj);
      this.listAISShow = this.listAIS.slice(
        obj.limit * (obj.page - 1),
        obj.limit * obj.page
      );
      this.handleGetVesselGroup(obj.page, obj.limit);
    },

    openDialogLoadListVesselGroup() {
      this.vesselGroup.isShowvesselGroupGrid = true;
    },

    openDialogListais() {
      this.isShowListAisDialog = true;
    },
    closeDialogListais() {
      this.isShowListAisDialog = false;
    },

    async handleGetVesselGroup(currentpage, rowsPerPage) {
      console.log("GetVesselGroup");
      const overallApi = new OverallApi();
      var resListVesselGroup = await overallApi.searchVesselGroup(
        "/ais/object-group/search-list",
        {
          startTime: this.vesselGroupQuery.startTime,
          endTime: this.vesselGroupQuery.endTime,
          currentPage: currentpage
            ? currentpage
            : this.vesselGroupQuery.currentPage,
          rowsPerPage: rowsPerPage
            ? rowsPerPage
            : this.vesselGroupQuery.rowsPerPage,
        }
      );
      if (resListVesselGroup.status == 200) {
        this.lstVesselGroup = resListVesselGroup.data;
        this.vesselGroupQuery.total = resListVesselGroup.total;
      }

      console.log(resListVesselGroup);
    },

    async handleGetListArea() {
      console.log("handleGetListArea");
      const overallApi = new OverallApi();
      var resListArea = await overallApi.searchVesselGroup(
        "/map/get-list-area",
        {}
      );
      if (resListArea.status == 200) {
        this.lstArea = resListArea.data;
      }

      console.log(resListArea);
    },

    getListAIS(obj) {
      console.log(obj);
      this.listAISShow = this.listAIS.slice(
        obj.limit * (obj.page - 1),
        obj.limit * obj.page
      );
    },

    closerPopup() {
      overLayPopup.setPosition(undefined);
      return false;
    },

    handleDraw() {
      currentDraw = new Draw({
        source: currentDrawSource,
        type: "LineString",
      });
      map.addInteraction(currentDraw);
    },
    handleEndDraw() {
      try {
        this.addSelect();
        this.vung.isDrawing = false;
        map.removeInteraction(currentDraw);
        // var features = currentDraw.source_.getFeatures();
        // var newForm = new WKT();
        // // new WKT().readFeature(linestringWKT);
        // var arrWKTFeature = [];
        // for (var i = 0; i < features.length; i++) {
        //   var featColl = newForm.writeGeometry(features[i].getGeometry());
        //   arrWKTFeature.push(featColl);
        // }
        // console.log("data vung: ", arrWKTFeature);

        this.translate = new Translate({
          features: this.singleClick.getFeatures(),
        });
        console.log("add translate feature");
        map.addInteraction(this.translate);
      } catch (error) {
        console.log("end draw error ", error.message);
      }
    },

    async saveDraw() {
      try {
        var features = currentDraw.source_.getFeatures();
        var newForm = new WKT();
        // new WKT().readFeature(linestringWKT);
        var arrWKTFeature = [];
        var area = null;
        for (var i = 0; i < features.length; i++) {
          area = {};
          var wktValue = newForm.writeGeometry(features[i].getGeometry());
          area.name = features[i].values_.name;
          area.value = wktValue;
          area.type = 0;
          arrWKTFeature.push(area);
        }

        const overallApi = new OverallApi();
        var res = await overallApi.saveDraw("/map/save-area", {
          listAddAreaRequest: arrWKTFeature,
        });
        console.log("data vung: ", arrWKTFeature);
        this.$notify({
          title: "Lưu vùng thành công",

          type: "success",
        });
      } catch (error) {
        this.$notify({
          title: "Lưu vùng thất bại",

          type: "error",
        });
        console.log("save  draw error ", error);
      }
    },

    openDialogLoadListVessel() {
      this.isShowDialog = true;
    },

    drawLineStringGroup(data) {
      if (data && data.length > 0) {
        if (data.length > 1000) {
          this.$notify({
            title: "Vượt quá số lượng cho phép vẽ hành trình",

            type: "error",
          });
          return;
        }
        for (var i = 0; i < data.length; i++) {
          var _itemI = data[i];
          var arrLineString = [];
          arrLineString.push(_itemI);
          for (var j = i + 1; j < data.length; j++) {
            var _itemJ = data[j];
            if (_itemI.objId == _itemJ.objId) {
              arrLineString.push(_itemJ);
            }
          }

          if (arrLineString.length >= 2) {
            this.handleLineStringDraw(arrLineString);
          }
        }
      }
    },

    handleLineStringDraw(data) {
      var geojsonObject = {
        type: "FeatureCollection",
        crs: {
          type: "name",
          properties: {
            name: "EPSG:3857",
          },
        },
        features: [
          {
            type: "Feature",
            geometry: {
              type: "LineString",
              coordinates: [],
            },
          },
        ],
      };
      var arrVessel = [];

      for (var i = 0; i < data.length; i++) {
        var coordinate = [data[i].longitude, data[i].latitude];
        geojsonObject.features[0].geometry.coordinates.push(coordinate);

        var vesselPoint = new Point(
          convertCoordinates(data[i].longitude, data[i].latitude)
        );
        // vesselPoint.rotate(0.5);
        arrVessel.push(
          new Feature({
            geometry: vesselPoint,
            data: data[i],
            label: "Tàu",
          })
        );
        // map.addLayer(vessel);
      }
      if (geojsonObject.features[0].geometry.coordinates.length == 1) {
        geojsonObject.features[0].geometry.coordinates.push(
          geojsonObject.features[0].geometry.coordinates[0]
        );
      }
      var vectorSource = new VectorSource({
        features: new GeoJSON().readFeatures(geojsonObject, {
          dataProjection: "EPSG:4326",
          // featureProjection: "EPSG:3857",
        }),
      });

      var vectorLayer = new VectorLayer({
        source: vectorSource,
        style: styleFunction,
      });

      var layerPointVessel = new VectorLayer({
        source: new VectorSource({
          features: arrVessel,
        }),

        style: function (feature) {
          return new Style({
            image: new Icon({
              anchor: [0.5, 0.5],
              scale: 1,
              src: require("@/assets/icon/vssel.png"),
              // "http://192.168.61.106:9985/vsat@/assets/icon/object/Yacht.png",
              // rotation: (feature.values_.data.cog * Math.PI) / 180,
              rotateWithView: true,
            }),
          });
        },
      });

      map.addLayer(vectorLayer);
      map.addLayer(layerPointVessel);
    },

    async handleClutersMake(drawLineStringGroup) {
      this.clearAllFeature();

      const overallApi = new OverallApi();
      var listAIS;
      try {
        console.log(this.query);
        if (this.query.startTime == null || this.query.endTime == null) {
          this.$notify({
            title: "Lỗi",
            message: "Thời gian là bắt buộc",

            type: "error",
          });
          return;
        }
        console.log("this.query.vesselType ", this.query.vesselType);
        listAIS = await overallApi.searchListAis("/ais/search-list-general", {
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          groupIds: this.query.vesselGroup ? [this.query.vesselGroup.id] : null,
          dataSource: this.query.dataSource,
          sourceIps: this.query.sourceIps?this.query.sourceIps.trim():this.query.sourceIps,
          destIps: this.query.destIps?this.query.destIps.trim():this.query.destIps,
          countryId: this.query.country ? this.query.country.id : null,
          typeId: this.query.vesselType ? this.query.vesselType.id : null,
          mmsi: this.query.mmsi,
          isUfo: this.query.typeObject,
          areaIds: this.query.area ? [this.query.area.id] : null,
          // startTime: "2021-03-24 07:35:00",
          // endTime: "2021-03-24 23:00:00",

          limit: 500000,
        });
        console.log("res query ", listAIS);
        if (listAIS.status == 200) {
          // this.$notify({
          //   message: "Tìm thấy " + listAIS.data.length + " vị trí.",

          //   type: "success",
          // });
          if (listAIS.data == null || listAIS.data.length == 0) {
            this.listAISShow = [];
            this.query.total = 0;
            return;
          }
          this.closeDialogSearchAisvessel();
          this.listAIS = listAIS.data;
          this.listAISShow = this.listAIS.slice(0, this.query.rowsPerPage);
          this.query.total = listAIS.data.length;
          // this.isShowDialog = false;
          this.lstVesselFeature = new Array(listAIS.data.length);

          for (var i = 0; i < listAIS.data.length; ++i) {
            var data = listAIS.data[i];
            var coordinates = [data.longitude, data.latitude];
            // lstVesselFeature[i] = new Feature(new Point(coordinates));
            // var coordinates = [
            //   2 * e * Math.random() - e,
            //   2 * e * Math.random() - e,
            // ];
            var vesselPoint = new Point(
              convertCoordinates(data.longitude, data.latitude)
            );
            // vesselPoint.rotate(0.5)
            this.lstVesselFeature[i] = new Feature({
              geometry: vesselPoint,

              data: data,
              label: "Tàu",
            });

            // console.log("draw all line", data);
            // this.handleLineStringDraw(data);
          }
          this.centerToPoisition();
        } else {
          alert("Get list vessel error.");
          return;
        }
        if (!drawLineStringGroup) {
          vector = new VectorLayer({
            source: new Cluster({
              distance: 80,
              source: new VectorSource({
                // format: new GeoJSON(),
                features: this.lstVesselFeature,
                // url:
                //   "https://openlayers.org/en/latest/examples/data/kml/2012_Earthquakes_Mag5.kml",
                // format: new KML({
                //   extractStyles: false,
                // }),
              }),
            }),
            style: styleFunctionClutter,
            isCluster: true,
          });
          map.addLayer(vector);
        } else {
          this.drawLineStringGroup(this.listAIS);
        }
      } catch (error) {
        console.log("get list ais fail: ", error.message);
        this.$notify({
          title: "Lỗi",
          message: "Lấy danh sách tàu thất bại",

          type: "error",
        });
      }
    },

    clearAllFeature() {
      try {
        const layers = [...map.getLayers().getArray()];

        console.log("clear all 1", layers);
        layers.splice(0, 1);
        console.log("clear all 2", layers);
        layers.forEach((layer) => {
          if (layer.values_.isCluster) {
            map.removeLayer(layer);
          }
        });

        map.removeInteraction(this.translate);
        map.removeInteraction(this.singleClick);
        map.removeInteraction(currentDraw);
        currentDrawSource = new VectorSource({ wrapX: false });
        map.addLayer(
          new VectorLayer({
            source: currentDrawSource,
          })
        );
        currentDraw = null;
      } catch (error) {
        console.log("error clear all feature");
      }
    },

    clearAllFeatureDrawMeasure() {
      try {
        const layers = [...map.getLayers().getArray()];

        layers.splice(0, 1);
        console.log("clear all 1", layers);
        layers.forEach((layer) => {
          if (layer.values_.isMeasure) {
            map.removeLayer(layer);
          }
        });
        console.log("clear all 2", layers);

        map.removeInteraction(draw);
        map.removeLayer(vectorMeasure);
        sourceMeasure = new VectorSource({ wrapX: false });
        map.addLayer(
          new VectorLayer({
            isMeasure: true,
            source: sourceMeasure,
            style: new Style({
              fill: new Fill({
                color: "#748fc5a8",
              }),
              stroke: new Stroke({
                color: "#ffcc33",
                width: 2,
              }),
              image: new CircleStyle({
                radius: 7,
                fill: new Fill({
                  color: "#ffcc33",
                }),
              }),
            }),
          })
        );
        draw = null;
      } catch (error) {
        console.log("error clear all feature");
      }
    },

    centerToPoisition(longitude, latitude, zoom) {
      // console.log("re center: ", map.getSize());
      var location = hanoi;
      if (longitude && latitude) {
        location = [longitude, latitude];
      }
      var view = new View({
        projection: "EPSG:4326",
        center: location,
        zoom: zoom ? zoom : 5,
      });
      view.centerOn(location, map.getSize(), [
        map.getSize()[0] / 2,
        map.getSize()[1] / 2 + 220,
      ]);
      map.setView(view);
    },

    /**
     * Handle pointer move.
     * @param {import("../src/ol/MapBrowserEvent").default} evt The event.
     */
    pointerMoveHandler(evt) {
      if (evt.dragging) {
        return;
      }
      /** @type {string} */
      var helpMsg = "Click to start drawing";

      if (sketch) {
        var geom = sketch.getGeometry();
        if (geom instanceof Polygon) {
          helpMsg = continuePolygonMsg;
        } else if (geom instanceof LineString) {
          helpMsg = continueLineMsg;
        }
      }

      helpTooltipElement.innerHTML = helpMsg;
      helpTooltip.setPosition(evt.coordinate);

      helpTooltipElement.classList.remove("hidden");
    },

    /**
     * Format length output.
     * @param {LineString} line The line.
     * @return {string} The formatted length.
     */
    formatLength(line) {
      var length = getLength(line);
      console.log("measure length: ", length);
      var output;
      output = Math.round((length / 1000) * 100) / 100 + " " + "km";
      if (length > 1) {
        output = Math.round(length * 100) + " " + "km";
      } else {
        output = Math.round(length * 100) + " " + "m";
      }
      return output;
    },

    /**
     * Format area output.
     * @param {Polygon} polygon The polygon.
     * @return {string} Formatted area.
     */
    formatArea(polygon) {
      var area = getArea(polygon);
      console.log("area length: ", area);
      var output;
      if (area > 10) {
        output = Math.round(area * 1000000) / 100 + " " + "km<sup>2</sup>";
      } else {
        output = Math.round(area * 1000000) + " " + "m<sup>2</sup>";
      }
      return output;
    },

    addInteractionMeasure(type) {
      // debugger;
      // map.on("pointermove", this.pointerMoveHandler.bind(this));

      // map.getViewport().addEventListener("mouseout", function () {
      //   helpTooltipElement.classList.add("hidden");
      // });

      draw = new Draw({
        drawType: "MEASURE",
        source: sourceMeasure,
        type: type,
        style: new Style({
          fill: new Fill({
            color: "rgba(255, 255, 255, 0.2)",
          }),
          stroke: new Stroke({
            color: "rgba(0, 0, 0, 0.5)",
            lineDash: [10, 10],
            width: 2,
          }),
          image: new CircleStyle({
            radius: 5,
            stroke: new Stroke({
              color: "rgba(0, 0, 0, 0.7)",
            }),
            fill: new Fill({
              color: "rgba(255, 255, 255, 0.2)",
            }),
          }),
        }),
      });
      map.addInteraction(draw);

      this.createMeasureTooltip();
      this.createHelpTooltip();

      draw.on("drawstart", this.drawstartMeasure.bind(this));

      draw.on("drawend", () => {
        measureTooltipElement.className = "ol-tooltip ol-tooltip-static";
        measureTooltip.setOffset([0, -7]);
        // unset sketch
        sketch = null;
        // unset tooltip so that a new one can be created
        measureTooltipElement = null;
        this.createMeasureTooltip();
        unByKey(listener);
      });
    },

    drawstartMeasure(evt) {
      // set sketch
      sketch = evt.feature;

      /** @type {import("../src/ol/coordinate.js").Coordinate|undefined} */
      var tooltipCoord = evt.coordinate;

      listener = sketch.getGeometry().on("change", (evt) => {
        var geom = evt.target;
        var output;
        if (geom instanceof Polygon) {
          output = this.formatArea(geom);
          tooltipCoord = geom.getInteriorPoint().getCoordinates();
        } else if (geom instanceof LineString) {
          output = this.formatLength(geom);
          tooltipCoord = geom.getLastCoordinate();
        }
        measureTooltipElement.innerHTML = output;
        measureTooltip.setPosition(tooltipCoord);
      });
    },

    /**
     * Creates a new help tooltip
     */
    createHelpTooltip() {
      if (helpTooltipElement) {
        helpTooltipElement.parentNode.removeChild(helpTooltipElement);
      }
      helpTooltipElement = document.createElement("div");
      helpTooltipElement.className = "ol-tooltip hidden";
      helpTooltip = new Overlay({
        id: {
          isMeasure: true,
        },
        element: helpTooltipElement,
        offset: [15, 0],
        positioning: "center-left",
      });
      map.addOverlay(helpTooltip);
    },

    /**
     * Creates a new measure tooltip
     */
    createMeasureTooltip() {
      if (measureTooltipElement) {
        measureTooltipElement.parentNode.removeChild(measureTooltipElement);
      }
      measureTooltipElement = document.createElement("div");
      measureTooltipElement.className = "ol-tooltip ol-tooltip-measure";
      measureTooltip = new Overlay({
        id: {
          isMeasure: true,
        },
        element: measureTooltipElement,
        offset: [0, -15],
        positioning: "bottom-center",
      });
      map.addOverlay(measureTooltip);
    },

    measureMap(type) {
      if (draw != null) {
        this.cancelMeasureMap();
      }
      if (type == "LineString") {
        this.isMeasureMapDraw = true;
        this.isMeasureDientichMapDraw = false;
        this.addInteractionMeasure(type);
      } else if (type == "Polygon") {
        this.isMeasureMapDraw = false;
        this.isMeasureDientichMapDraw = true;
        this.addInteractionMeasure(type);
      }
    },

    clearOverLay() {
      map
        .getOverlays()
        .getArray()
        .slice(0)
        .forEach(function (overlay) {
          map.removeOverlay(overlay);
        });
    },

    cancelMeasureMap() {
      this.isMeasureMapDraw = false;
      this.isMeasureDientichMapDraw = false;
      this.clearAllFeatureDrawMeasure();
      map
        .getOverlays()
        .getArray()
        .slice(0)
        .forEach(function (overlay) {
          console.log("remove ol ", overlay);
          if (overlay.id && overlay.id.isMeasure) {
            map.removeOverlay(overlay);
            console.log("remove ol del ", overlay);
          }
        });
    },

    handleDrawVung() {
      if (this.vung.name == "" || this.vung.name == null) {
        this.$notify({
          title: "Lỗi",
          message: "Tên vùng là bắt buộc",

          type: "error",
        });
        return;
      }

      this.isShowVung = false;
      var value = this.vung.vungType;
      console.log("type ", value);
      if (value && value !== "None") {
        this.vung.isDrawing = true;
        currentDraw = new Draw({
          source: source,
          type: value,
          freehand: true,
        });
        currentDraw.on("drawend", (event) => {
          this.featureID = this.featureID + 1;
          console.log("drawend", this);
          event.feature.setProperties({
            id: this.featureID,
            name: "" + this.vung.name + "",
          });
          this.vung.name = "";
          this.handleEndDraw();
        });
        map.addInteraction(currentDraw);

        // var modify = new Modify({ source: currentDrawSource });
        // map.addInteraction(modify);

        var snap = new Snap({ source: currentDrawSource });
        map.addInteraction(snap);
      }
      this.addInteraction();
    },

    async handGetListAreaDraw() {
      try {
        if (!this.areaQuery.startTime || !this.areaQuery.endTime) {
          this.$notify({
            title: "Lỗi",
            message: "StartTime và EndTime là bắt buộc",

            type: "error",
          });
          return;
        }
        const overallApi = new OverallApi();
        var res = await overallApi.saveDraw("/map/get-list-area", {
          startTime: this.areaQuery.startTime,
          endTime: this.areaQuery.endTime,
        });
        if (res.status == 200) {
          this.vung.isShowDialogQuery = false;
          var arrFeature = [];
          for (var i = 0; i < res.data.length; i++) {
            try {
              var wkt = res.data[i].value;
              // "POLYGON((126.84244527822119 18.77993158494867,126.84244527822119 18.77993158494867,126.43195497035344 18.77993158494867,126.2267098164196 18.77993158494867,126.02146466248573 18.77993158494867,125.40572920068414 19.190421892816403,125.20048404675026 19.190421892816403,124.78999373888254 19.395667046750262,124.58474858494867 19.395667046750262,124.58474858494867 19.395667046750262,123.96901312314708 19.600912200684135,123.7637679692132 19.600912200684135,123.14803250741161 19.600912200684135,122.73754219954387 19.600912200684135,122.73754219954387 19.600912200684135,122.12180673774228 19.600912200684135,122.12180673774228 19.600912200684135,121.50607127594068 19.600912200684135,121.50607127594068 19.600912200684135,121.09558096807297 19.600912200684135,120.89033581413909 19.600912200684135,120.89033581413909 19.600912200684135,120.2746003523375 19.600912200684135,120.06935519840363 19.600912200684135,119.65886489053591 19.600912200684135,119.45361973660204 19.600912200684135,119.04312942873429 19.395667046750262,118.63263912086657 19.190421892816403,118.22214881299885 19.190421892816403,118.01690365906498 18.985176738882544,117.8116585051311 18.77993158494867,117.8116585051311 18.16419612314708,117.8116585051311 17.958950969213205,117.8116585051311 17.958950969213205,117.8116585051311 17.548460661345473,117.8116585051311 17.548460661345473,117.8116585051311 17.137970353477755,117.8116585051311 16.93272519954388,117.8116585051311 16.52223489167615,117.8116585051311 16.52223489167615,117.8116585051311 16.111744583808417,117.8116585051311 15.701254275940698,118.01690365906498 15.496009122006825,118.01690365906498 15.085518814139093,118.01690365906498 15.085518814139093,118.01690365906498 14.67502850627136,118.01690365906498 14.67502850627136,118.22214881299885 14.264538198403628,118.22214881299885 14.264538198403628,118.22214881299885 13.85404789053591,118.22214881299885 13.648802736602036,118.4273939669327 13.443557582668177,118.63263912086657 13.238312428734304,118.63263912086657 13.238312428734304,118.63263912086657 12.827822120866571,118.63263912086657 12.827822120866571,118.83788427480044 12.417331812998839,118.83788427480044 12.417331812998839,118.83788427480044 12.00684150513112,118.83788427480044 12.00684150513112,119.04312942873429 11.596351197263388,119.04312942873429 11.185860889395656,119.24837458266816 10.57012542759405,119.24837458266816 10.57012542759405,119.45361973660204 10.36488027366019,119.65886489053591 9.954389965792458,119.65886489053591 9.954389965792458,120.06935519840363 9.338654503990867,120.06935519840363 9.133409350056994,120.2746003523375 8.928164196123134,120.2746003523375 8.722919042189261,120.47984550627137 8.517673888255402,120.68509066020522 8.312428734321543,120.89033581413909 8.10718358038767,120.89033581413909 8.10718358038767,121.30082612200681 7.491448118586078,121.50607127594068 7.286202964652205,121.71131642987456 7.286202964652205,121.71131642987456 7.286202964652205,121.91656158380843 7.0809578107183455,121.91656158380843 6.875712656784472,122.32705189167615 6.670467502850613,122.53229704561002 6.465222348916754,122.73754219954387 6.259977194982881,123.14803250741161 6.259977194982881,123.35327766134549 6.259977194982881,123.96901312314708 5.849486887115148,124.3795034310148 5.644241733181289,124.78999373888254 5.438996579247416,125.40572920068414 5.233751425313557,125.81621950855185 5.028506271379683,125.81621950855185 5.028506271379683,126.2267098164196 4.823261117445824,126.2267098164196 4.823261117445824,126.63720012428732 4.823261117445824,126.63720012428732 4.823261117445824,127.04769043215506 4.823261117445824,127.04769043215506 4.823261117445824,127.45818074002278 4.823261117445824,127.66342589395666 4.823261117445824,127.66342589395666 4.823261117445824,128.07391620182437 4.823261117445824,128.48440650969212 4.823261117445824,128.68965166362597 5.028506271379683,129.1001419714937 5.233751425313557,129.1001419714937 5.233751425313557,129.51063227936143 5.233751425313557,129.51063227936143 5.233751425313557,129.92112258722918 5.233751425313557,130.12636774116302 5.438996579247416,130.53685804903077 5.438996579247416,130.53685804903077 5.644241733181289,130.74210320296464 5.849486887115148,131.15259351083236 6.0547320410490215,131.35783866476623 6.259977194982881,131.35783866476623 6.259977194982881,131.76832897263395 6.670467502850613,131.97357412656783 7.0809578107183455,132.1788192805017 7.286202964652205,132.38406443443554 7.491448118586078,132.58930958836942 7.696693272519937,132.58930958836942 7.696693272519937,132.7945547423033 8.312428734321543,132.99979989623716 8.517673888255402,133.205045050171 8.722919042189261,133.41029020410488 9.133409350056994,133.41029020410488 9.338654503990867,133.41029020410488 9.338654503990867,133.41029020410488 9.954389965792458,133.41029020410488 9.954389965792458,133.41029020410488 10.36488027366019,133.41029020410488 10.775370581527923,133.41029020410488 10.775370581527923,133.41029020410488 11.185860889395656,133.41029020410488 11.596351197263388,133.41029020410488 11.596351197263388,133.41029020410488 12.00684150513112,133.41029020410488 12.00684150513112,133.41029020410488 12.417331812998839,133.41029020410488 12.827822120866571,133.41029020410488 13.033067274800445,133.205045050171 13.238312428734304,132.99979989623716 13.648802736602036,132.7945547423033 14.059293044469769,132.58930958836942 14.469783352337501,132.58930958836942 14.469783352337501,132.38406443443554 14.67502850627136,132.38406443443554 14.67502850627136,132.1788192805017 15.085518814139093,131.97357412656783 15.085518814139093,131.76832897263395 15.290763968072966,131.35783866476623 15.701254275940698,131.15259351083236 16.111744583808417,131.15259351083236 16.111744583808417,130.74210320296464 16.52223489167615,130.3316128950969 16.93272519954388,130.12636774116302 17.137970353477755,130.12636774116302 17.137970353477755,129.92112258722918 17.548460661345473,129.7158774332953 17.548460661345473,129.51063227936143 17.753705815279346,129.51063227936143 17.753705815279346,128.89489681755984 18.16419612314708,128.89489681755984 18.16419612314708,128.48440650969212 18.16419612314708,128.07391620182437 18.16419612314708,127.86867104789053 18.369441277080938,127.45818074002278 18.57468643101481,127.45818074002278 18.57468643101481,126.84244527822119 18.77993158494867,126.84244527822119 18.77993158494867,126.43195497035344 18.77993158494867,126.84244527822119 18.77993158494867))";

              var format = new WKT();

              var feature = format.readFeature(wkt, {
                dataProjection: "EPSG:4326",
                featureProjection: "EPSG:4326",
              });
              feature.data = res.data[i];
              feature.type = "area";
              arrFeature.push(feature);
            } catch (e) {
              console.log("draw area error ", res.data);
            }
          }
          var vector = new VectorLayer({
            source: new VectorSource({
              features: arrFeature,
            }),
          });
          map.addLayer(vector);
        }
        console.log("data vung: ", res);
      } catch (error) {
        console.log(error);
        this.$notify({
          title: "Lỗi",
          message: "Get list area thất bại.",

          type: "error",
        });
      }
    },

    handleDeleteFeature() {
      this.removeFeature();
    },
    addInteraction() {
      map.removeInteraction(this.singleClick);
    },
    addSelect() {
      map.removeInteraction();
      this.singleClick = new Select();
      map.addInteraction(this.singleClick);

      this.singleClick.getFeatures().on("add", (event) => {
        var properties = event.element.getProperties();
        this.selectedFeatureID = properties.id;
        console.log("select feature,", properties);
      });
      this.singleClick.getFeatures().on("remove", (event) => {
        this.selectedFeatureID = null;
        console.log("deselected", event);
      });
    },

    removeFeature() {
      var features = currentDrawSource.getFeatures();
      if (features != null && features.length > 0) {
        for (var x in features) {
          var properties = features[x].getProperties();
          console.log(properties);
          var id = properties.id;
          if (id == this.selectedFeatureID) {
            console.log("remove: ", properties);
            currentDrawSource.removeFeature(features[x]);
            this.selectedFeatureID = null;
            break;
          }
        }
      }
    },

    async historyTabActive() {
      console.log(
        "chi tiết thông tin media Object id: ",
        this.vesselDetailDialog
      );
      const overallApi = new OverallApi();

      try {
        let res = await overallApi.postApi("/media/total-by-object", {
          // TODO hard code to test
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          objId: this.vesselDetailDialog.isUfo == 0 ? this.vesselDetailDialog.objId : this.objectDetailDialog.objId,
          // startTime: "2021-03-02 07:05:24",
          // endTime: "2021-03-04 07:05:24",
          // objId: "412333612",
        });

        console.log("/media/total-by-object ==> res: ", res);
        if (res && res.status == 200 && res.total !== undefined)
          this.totalInfoMedia = res.total;
      } catch (error) {
        this.totalInfoMedia = 0;
        console.error(error);
      }

      try {
        let res = await overallApi.postApi("/rule/event-count", {
          // TODO hard code to test
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          objId: this.vesselDetailDialog.isUfo == 0 ? this.vesselDetailDialog.objId : this.objectDetailDialog.objId,
          // startTime: "2021-04-25 08:45:56",
          // endTime: "2021-12-30 08:45:57",
          // objId: "413903130",
        });
        console.log("/rule/event-count ==> res: ", res);
        if (res && res.status == 200 && res.data !== undefined)
          this.totalInfoEvent = res.data;
      } catch (error) {
        this.totalInfoEvent = 0;
        console.error(error);
      }
    },

    async detailMedia() {
      this.openMediaInfoDialog();

      // Chuẩn
      // let payLoad = {
      //   startTime: this.query.startTime,
      //   endTime: this.query.endTime,
      //   objId: this.vesselDetailDialog.isUfo == 0 ? this.vesselDetailDialog.objId : this.objectDetailDialog.objId,
      //   currentPage: 1,
      //   rowsPerPage: 1000, // Không cần phân trang cho danh sách media theo đối tượng, vì nó rất ít data.
      // };

      // Video
      let payLoad = {
        // TODO hard code to test
        startTime: "2021-03-02 07:05:24", //this.query.startTime,
        endTime: "2021-03-04 07:05:24", //this.query.endTime,
        objId: "412333612", // this.vesselDetailDialog.objId,
        currentPage: 1,
        rowsPerPage: 1000,
      };

      // Audio
      // let payLoad = {
      //   startTime: "2021-04-02 00:00:00",
      //   endTime: "2021-04-02 09:00:00",
      //   objId: "412333613", // 412333614
      //   currentPage: 1,
      //   rowsPerPage: 100,
      // };

      // Web
      // let payLoad = {
      //   startTime: "2021-04-12 00:00:00",
      //   endTime: "2021-04-12 09:00:00",
      //   objId: "148", // 413454580
      //   currentPage: 1,
      //   rowsPerPage: 100,
      // };

      // Email
      // let payLoad = {
      //   startTime: "2021-04-02 00:00:00",
      //   endTime: "2021-04-02 09:00:00",
      //   objId: "413493740",
      //   currentPage: 1,
      //   rowsPerPage: 100,
      // };

      // Undefined
      // let payLoad = {
      //   startTime: "2021-04-12 00:00:00",
      //   endTime: "2021-04-12 09:00:00",
      //   objId: "412477720", // 412002304
      //   currentPage: 1,
      //   rowsPerPage: 100,
      // };

      // console.log("this.objectDetailDialog: ", this.objectDetailDialog);
      // console.log("this.vesselDetailDialog: ", this.vesselDetailDialog);

      console.log("/media/list/search ==> req: ", payLoad);
      let res = await new OverallApi().postApi("/media/list/search", payLoad);
      console.log("/media/list/search <== res: ", res);

      if (res.status == 200) this.mediaInfo.lstMedia = res.data;
    },

    async detailEvent() {
      this.openRuleEventInfoDialog();

      // Chuẩn
      let payLoad = {
        // TODO hard code to test
        // startTime: this.query.startTime,
        // endTime: this.query.endTime,
        // objId: this.vesselDetailDialog.isUfo == 0 ? this.vesselDetailDialog.objId : this.objectDetailDialog.objId,
        startTime: "2021-04-25 08:45:56",
        endTime: "2021-12-30 08:45:57",
        objId: "413903130",
        currentPage: 1,
        rowsPerPage: 1000, // Không cần phân trang cho danh sách sự kiện theo đối tượng, vì nó rất ít data.
      };

      // Video
      // let payLoad = {
      //   startTime: "2021-03-02 07:05:24", //this.query.startTime,
      //   endTime: "2021-03-04 07:05:24", //this.query.endTime,
      //   objId: "412333612", // this.vesselDetailDialog.objId,
      //   currentPage: 1,
      //   rowsPerPage: 1000,
      // };

      // Audio
      // let payLoad = {
      //   startTime: "2021-04-02 00:00:00",
      //   endTime: "2021-04-02 09:00:00",
      //   objId: "412333613", // 412333614
      //   currentPage: 1,
      //   rowsPerPage: 100,
      // };

      // Web
      // let payLoad = {
      //   startTime: "2021-04-12 00:00:00",
      //   endTime: "2021-04-12 09:00:00",
      //   objId: "148", // 413454580
      //   currentPage: 1,
      //   rowsPerPage: 100,
      // };

      // Email
      // let payLoad = {
      //   startTime: "2021-04-02 00:00:00",
      //   endTime: "2021-04-02 09:00:00",
      //   objId: "413493740",
      //   currentPage: 1,
      //   rowsPerPage: 100,
      // };

      // Undefined
      // let payLoad = {
      //   startTime: "2021-04-12 00:00:00",
      //   endTime: "2021-04-12 09:00:00",
      //   objId: "412477720", // 412002304
      //   currentPage: 1,
      //   rowsPerPage: 100,
      // };

      console.log("/rule/event-list ==> req: ", payLoad);
      let res = await new OverallApi().postApi("/rule/event-list", payLoad);
      console.log("/rule/event-list <== res: ", res);

      if (res.status == 200) this.ruleEventInfo.lstRuleEvent = res.data;
    },

    /* Feature moving */
    doDrag(feature, pixel) {
      console.log("do drag");
      // for (var f in selectedFeatures) {
      //   if (feature != selectedFeatures[f]) {
      //     var res = map.getResolution();
      //     selectedFeatures[f].geometry.move(
      //       res * (pixel.x - lastPixel.x),
      //       res * (lastPixel.y - pixel.y)
      //     );
      //     vectors.drawFeature(selectedFeatures[f]);
      //   }
      // }
      // lastPixel = pixel;
    },

    /* Featrue stopped moving */
    endDrag(feature, pixel) {
      console.log("end drag");
      // for (f in selectedFeatures) {
      //   f.state = OpenLayers.State.UPDATE;
      // }
    },
    closeDialogPlayMedia() {
      stopPlayMedia("mediaPlayerId");
    },
  },
};

// First some change in this function.
</script>

<style lang="scss">
@import "~ol/ol.css";
.custom-mouse-position {
  position: absolute;
  bottom: 10px;
  right: 30px;
  float: right;
  width: 170px;
  height: 40px;
}
.el-button-on-map {
  width: 70px;
  position: absolute;
  bottom: 50px;
  right: 6px;
  z-index: 10;
  text-align: right;
}
.el-button-on-map button {
  margin-bottom: 10px;
}
#mapOL {
  width: 100%;
  height: calc(100% - 60px);
  position: fixed;
}
.ol-popup {
  position: absolute;
  background-color: white;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
  padding: 15px;
  border-radius: 10px;
  border: 1px solid #cccccc;
  bottom: 12px;
  // top: 200px;
  // left: 200px;
  left: -500px;
  min-width: 445px !important;
}
.label-form {
  min-width: 70px;
  width: auto;
  float: left;
  margin-right: 6px;
}
.ol-popup label {
}
.el-button-on-map i {
  margin-left: -6px !important;
}
.ol-popup:after,
.ol-popup:before {
  top: 100%;
  border: solid transparent;
  content: " ";
  height: 0;
  width: 0;
  position: absolute;
  pointer-events: none;
}
.ol-popup:after {
  border-top-color: white;
  border-width: 10px;
  left: 48px;
  margin-left: -10px;
}
.ol-popup:before {
  border-top-color: #cccccc;
  border-width: 11px;
  left: 48px;
  margin-left: -11px;
}
.el-dialog__wrapper {
  left: calc(50% - 400px);
}
.el-dialog__wrapper {
  overflow: unset !important;
}
.ol-popup-closer {
  text-decoration: none;
  position: absolute;
  top: 2px;
  right: 8px;
}
.ol-popup-closer:after {
  content: "✖";
}
#dl-list-vessel-position {
  // margin-top: 10vh;
  // z-index: auto;
  // max-width: 700px;
  // min-width: 350px;
  // width: auto;
  // height: max-content;
  left: 69%;
}
#dl-list-vessel-position2 {
  width: 800px;
  max-height: 1000px;
  top: 10px;
  left: calc(35% - 400px);
}
.v-modal {
  position: unset;
}
#dl-list-vessel-position {
  width: 360px;
  max-height: 1000px;
}

.el-dialog__body {
  padding: 8px;
}

#dl-list-vessel-position .el-dialog__body {
  // position: relative;
  height: 600px;
  overflow: auto;
  overflow-x: hidden;
}

#dl-list-vessel-position2 .el-dialog__body {
  // position: relative;
  height: 473px;
  overflow: auto;
  overflow-x: hidden;
}
.el-dialog__footer {
  position: sticky;
  bottom: 0;
}

#dl-media-info {
  width: 840px;
}
.dialog-detail-vessel {
  padding-top: 6px;
}
.hideSidebar .el-icon-map-location {
  margin-left: 20px;
}
.pagination-container {
  overflow: auto;
}
.bt-search-advance {
  float: right;
}
.require {
  color: red;
}
.text-des {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.note-list {
  width: 100%;
  height: 225px !important;
  overflow: auto;
  border: 1px solid #cecece;
}
#popup {
  padding: 0;
  border-radius: 3px;
}
#popup #popup-content {
  padding: 6px;
  height: 422px !important;
}
.popup-header {
  height: 40px;
  background-color: #0d518c;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
}
.container-history {
  border: 1px solid #cecece;
}
.mt-3,
.my-3 {
  margin-top: 4px !important;
}
.info-history {
  width: 100%;
  height: 200px;
  text-align: center;
}
.icon-info {
  width: 70%;
  height: 80px;
  margin: auto;
}
.button-info {
  margin-top: 4px;
}
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
  margin-right: 5px;
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
.el-input__inner {
  // width: 150px;
}

.el-row {
  margin-bottom: 5px;
  &:last-child {
    margin-bottom: 0px;
  }
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.bg-purple {
  background: #f2f2f2;
}
.aliasLeft {
  text-align: left;
  padding-left: 5px;
  font-size: 11px;
  font-weight: bold;
}
.textRight {
  text-align: left;
  padding-left: 5px;
}
// .el-select>.el-input input {
//     //width: 225px;
//     width: 175px;
// }
// .el-input input {
//     width: 160px;
// }
#sltMediaType {
  width: 185px;
}
#sltDataSource {
  width: 175px;
}
#addCommentInput {
  // height: 32px;
  // line-height: 32px;
  width: 390px;
}
#startTimeInput {
  width: 185px;
}
#endTimeInput {
  width: 185px;
  // margin-left: 5px;
}
#btnSearch {
  padding-left: 30px;
}
</style>
<style>
.ol-tooltip {
  position: relative;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 4px;
  color: white;
  padding: 4px 8px;
  opacity: 0.7;
  white-space: nowrap;
  font-size: 12px;
}
.ol-tooltip-measure {
  opacity: 1;
  font-weight: bold;
}
.ol-tooltip-static {
  background-color: #ffcc33;
  color: black;
  border: 1px solid white;
}
#dialog-addToGroup{
  width:450px;
  left:calc(50% - 250px);
}
.ol-tooltip-measure:before,
.ol-tooltip-static:before {
  border-top: 6px solid rgba(0, 0, 0, 0.5);
  border-right: 6px solid transparent;
  border-left: 6px solid transparent;
  content: "";
  position: absolute;
  bottom: -6px;
  margin-left: -7px;
  left: 50%;
}
.ol-tooltip-static:before {
  border-top-color: #ffcc33;
}
#dl-list-vessel-position input {
  width: 100%;
}
.row {
  margin-right: 0px;
  margin-left: 0px;
}
.table-detail {
  border: 0.25px solid #cecece;
  margin-top: -1px;
  padding-top: 2px;
  padding-bottom: 2px;
}
.el-button-on-map button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.nav-link.active {
  background: #fff !important;
  border: none;
  border-top: 2px solid #11a0d9;
  border-radius: 0;
}
.nav-tabs .nav-link:hover,
.nav-tabs .nav-link:focus {
  border-color: #11a0d9;
  border-bottom: none;
  border-left: none;
  border-right: none;
  background-color: #fff;
}
.nav-link {
  color: black;
}
.nav-tabs .nav-link {
  margin-bottom: 0;
  background-color: #e0e0e0;
  border-bottom: none;
  border-left: none;
  border-right: none;
}
.nav-tabs {
  border-bottom: none;
}
.el-dialog__title {
  font-size: 15px;
}
textarea:focus-visible{
  outline: none;
}
</style>