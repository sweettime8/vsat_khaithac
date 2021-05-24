<template>
  <div id="voyage" style="width: 100%; height: 100%">
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
          Đối tượng: {{ vesselDetailDialog.objId }}
        </p>
      </div>
      <div
        id="popup-content"
        :style="[
          objectDetailDialog.isUfo == 1 ? { height: '300px !important' } : {},
        ]"
        v-show="isShowVesselDetailDialog"
      >
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
                <span>{{ vesselDetailDialog.countryName }}</span>
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
                <span
                  :style="[
                    vesselDetailDialog.status == 1
                      ? { color: '#28a12c' }
                      : { color: 'red' },
                  ]"
                  ><b>{{
                    vesselDetailDialog.status == 1
                      ? "Đang hoạt động"
                      : "Không hoạt động"
                  }}</b></span
                >
              </span>
            </b-row> -->
          </b-col>
        </b-row>
        <b-tabs content-class="mt-3" v-model="stepTabDetail">
          <b-tab title="Thông tin chung" active>
            <div v-if="vesselDetailDialog.isUfo == 0">
              <b-row>
                <b-col sm="12" class="table-detail text-des">
                  <span
                    ><b>Loại Tàu: </b>
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
                <b-col sm="6" class="table-detail text-des">
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
                <b-col sm="6" class="table-detail text-des">
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
                    ><b>IP Nguồn: </b>
                    <span>{{ vesselDetailDialog.sourceIp }}</span>
                  </span>
                </b-col>
                <b-col sm="6" class="table-detail text-des">
                  <span
                    ><b>IP Đích: </b>
                    <span id="vesselType">{{ vesselDetailDialog.destIp }}</span>
                  </span>
                </b-col>
              </b-row>
              <b-row>
                <b-col sm="12" class="table-detail text-des">
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
              <br />
              <!-- <b-row>
                <b-col sm="4">
                  <el-button
                    type="primary"
                    size="small"
                    @click="openDialogAddToGroup(vesselDetailDialog)"
                    >Thêm vào nhóm</el-button
                  >
                </b-col>
                <b-col sm="4">
                  <el-button
                    type="primary"
                    size="small"
                    @click="handleGetVoyageDetail(vesselDetailDialog)"
                    >Xem hành trình</el-button
                  >
                </b-col>
              </b-row> -->
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
              <!-- <b-row>
                <b-col sm="4">
                  <el-button
                    type="primary"
                    size="small"
                    @click="openDialogAddToGroup(vesselDetailDialog)"
                    >Thêm vào nhóm</el-button
                  >
                </b-col>
                <b-col sm="4">
                  <el-button
                    type="primary"
                    size="small"
                    @click="handleGetVoyageDetail(vesselDetailDialog)"
                    >Xem hành trình</el-button
                  >
                </b-col>
              </b-row> -->
            </div>
          </b-tab>
        </b-tabs>
      </div>
    </div>
    <div id="dateOverlay" class="date-overlay">
      <div class="date-overlay-wrapper">
        <b-row class="row">
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Từ ngày</label>
            </div>
            <el-date-picker
              id="jack"
              disabled
              v-model="query.startTime"
              size="small"
              format="dd/MM/yyyy HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :editable="false"
              style="width: 250px; margin-left: 10px"
              type="datetime"
              placeholder="Từ ngày"
            ></el-date-picker>
          </b-col>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Đến ngày </label>
            </div>
            <el-date-picker
              v-model="query.endTime"
              size="small"
              disabled
              format="dd/MM/yyyy HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :editable="false"
              style="width: 250px; margin-left: 10px"
              type="datetime"
              placeholder="Đến ngày"
            ></el-date-picker>
          </b-col>
        </b-row>
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
          style="background-position: center"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1177.png') + ')',
          }"
          @click="centerToPoisition(null, null, 3)"
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

      <!-- <el-button class="search" @click="clearAllFeature()" type="primary">
        <i class="el-icon-document-remove"></i>
      </el-button> -->
      <!-- <el-button
        class="search"
        @click="vung.isShowDialogQuery = true"
        type="primary"
      >
        <i class="el-icon-place"></i>
      </el-button>
      <el-button
        class="search"
        @click="handleDeleteFeature()"
        v-show="selectedFeatureID != null"
        type="primary"
      >
        <i class="el-icon-close"></i>
      </el-button> -->
      <!-- <el-button
        class="search"
        @click="editArea()"
        v-show="selectedFeatureID != null"
        type="primary"
      >
        <i class="el-icon-edit">Sửa vùng</i>
      </el-button> -->
      <!-- <el-button class="search" v-show="!vung.isDrawing" @click="saveDraw()" type="primary">
        <i class="el-icon-upload2">Lưu vùng</i>
      </el-button> -->
      <!-- <el-button
        class="search"
        @click="handleEndDraw()"
        v-show="vung.isDrawing"
        type="primary"
      >
        <i class="el-icon-close"></i>
      </el-button> -->

      <!-- <el-button
        class="search"
        @click="addArea()"
        v-show="!vung.isDrawing"
        type="primary"
      >
        <i class="el-icon-add">Tạo vùng</i>
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
          <!-- <i class="el-icon-search"></i> -->
        </el-button>
      </el-tooltip>
      <el-tooltip
        class="item"
        effect="dark"
        content="Tìm kiếm theo nhóm đối tượng"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1219.png') + ')',
          }"
          @click="openDialogLoadListByGroup()"
          type="primary"
        >
          <!-- <i class="el-icon-search"></i> -->
        </el-button>
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
          @click="openDialogListVessel()"
          type="primary"
        >
          <!-- <i class="el-icon-ship"></i> -->
        </el-button>
      </el-tooltip>

      <el-tooltip
        class="item"
        effect="dark"
        content="Play hành trình"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center"
          v-show="isPlayback == true"
          @click="toggleDialogPlayback()"
          type="primary"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1220.png') + ')',
          }"
        >
          <!-- <i class="el-icon-video-play"></i> -->
        </el-button>
      </el-tooltip>

      <!-- <el-tooltip
        class="item"
        effect="dark"
        content="Play hành trình new"
        placement="left-start"
      >
        <el-button class="search" @click="startAnimation()" type="primary">
          <i class="el-icon-video-play"></i>
        </el-button>
      </el-tooltip> -->

      <!-- <el-tooltip
        class="item"
        effect="dark"
        content="Play hành trình"
        placement="left-start"
      >
        <el-button
          class="search"
          @click="playbackVoyage()"
          type="primary"
          v-show="isPlayback == true"
        >
          <i class="el-icon-video-play"></i>
        </el-button>
      </el-tooltip> -->
      <el-tooltip
        class="item"
        effect="dark"
        content="Stop hành trình"
        placement="left-start"
      >
        <el-button
          class="search"
          @click="toggleDialogPlayback()"
          type="primary"
          v-show="isPlayback == false"
        >
          <i class="el-icon-video-pause"></i>
        </el-button>
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
      id="dl-list-vessel-voyage-position"
      class="el-bottom-search"
      v-dialogDrag
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
              style="width: 310px; margin-left: 10px"
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
              style="width: 310px; margin-left: 10px"
              type="datetime"
              placeholder="Đến ngày"
            ></el-date-picker>
          </b-col>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Loại đối tượng</label>
            </div>
            <el-select
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
        </b-row>
        <b-row>
          <b-col v-show="query.isAdvance" style="padding: 5px">
            <div class="label-form">
              <label>Quốc gia</label>
            </div>
            <el-select
              v-model="query.country"
              value-key="id"
              placeholder="Quốc gia"
              style="width: 310px; margin-left: 10px"
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
              v-model="query.area"
              value-key="id"
              placeholder="Vùng"
              style="width: 310px; margin-left: 10px"
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
        </b-row>
        <b-row>
          <b-col v-show="query.isAdvance" style="padding: 5px">
            <div class="label-form">
              <label>Loại tàu</label>
            </div>
            <el-select
              filterable
              v-model="query.vesselType"
              value-key="id"
              placeholder="Loại tàu"
              style="width: 310px; margin-left: 10px"
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
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Nhóm đối tượng</label>
            </div>
            <el-select
              v-model="query.vesselGroup"
              value-key="id"
              placeholder="Nhóm đối tượng"
              @change="handleChangeVesselGroupSelect"
              style="width: 310px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'Nhóm đối tượng'"
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
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Nguồn thu</label>
            </div>
            <!-- multiple -->
            <el-select
              id="sltDataSource"
              collapse-tags
              v-model="query.dataSource"
              clearable
              style="width: 310px; margin-left: 10px"
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
              <label>ID đối tượng</label>
            </div>
            <!-- multiple -->
            <el-input
              v-model="query.mmsi"
              type="text"
              name="mmsi"
              style="width: 310px; margin-left: 10px"
              placeholder="ID đối tượng"
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
              style="width: 310px; margin-left: 10px"
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
              style="width: 310px; margin-left: 10px"
              placeholder="IP Đích"
            />
          </b-col>
          <b-col style="padding: 5px">
            <!-- <div class="label-form">
              <label>Loại đối tượng</label>
            </div>
            <el-select
              id="sltTypeObject"
              collapse-tags
              v-model="query.typeObject"
              clearable
              style="width: 200px; margin-left: 10px"
              placeholder="loại đối tượng"
              size="small"
            >
              <el-option
                v-for="item in lstTypeObject"
                :key="item.id"
                :label="item.value"
                :value="item.id"
              ></el-option>
            </el-select> -->
          </b-col>
          <b-col style="padding: 5px"> </b-col>
        </b-row>
      </el-form>
      <br />

      <div slot="footer" class="dialog-footer search-footer">
        <el-button type="primary" @click="handleClutersMake()"
          >Tìm kiếm</el-button
        >
        <el-button @click="isShowDialog = false">Bỏ qua</el-button>
      </div>
    </el-dialog>
    <!-- danh sách đối tượng -->
    <el-dialog
      top="5vh"
      id="dl-list-vessel-voyage-position2"
      v-dialogDrag
      title="Danh sách đối tượng"
      :visible.sync="isShowDialogListAis"
      width="100%"
      :close-on-click-modal="false"
    >
      <el-table
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
        <el-table-column label="ID Đối tượng" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.objId }}</span>
          </template>
        </el-table-column>

        <el-table-column label="IP nguồn">
          <template slot-scope="scope">
            <span>{{ scope.row.sourceIp }}</span>
          </template>
        </el-table-column>
        <el-table-column label="IP Đích">
          <template slot-scope="scope">
            <span>{{ scope.row.destIp }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Loại đối tượng" style="text-align: center">
          <template slot-scope="scope">
            <span>{{ scope.row.isUfo == 1 ? "Đối tượng khác" : "Tàu" }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="Vĩ độ">
          <template slot-scope="scope">
            <span>{{ scope.row.latitude }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Kinh độ">
          <template slot-scope="scope">
            <span>{{ scope.row.longitude }}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="Tác động" width="85">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="
                closeDialogListVessel();
                handleListVesselVoyageDetail(scope.row);
              "
              >Chi tiết</el-button
            >
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
        <el-button @click="closeDialogListVessel()">Bỏ qua</el-button>
        <el-button
          v-if="isShowListObjectButton"
          type="primary"
          @click="handleListVesselVoyageGroup()"
          >Chi tiết danh sách đối tượng</el-button
        >
      </div>
    </el-dialog>
    <!-- grid ship -->
    <el-dialog
      v-dialogDrag
      top="5vh"
      title="Danh sách sách vị trí đối tượng"
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

    <!-- grid nhóm đối tượng -->
    <el-dialog
      v-dialogDrag
      top="5vh"
      title="Danh sách sách nhóm đối tượng"
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
        id="my-table"
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

        <el-table-column label="Tên nhóm đối tượng">
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
    <el-dialog
      id="dl-ListVoyageDialog"
      v-dialogDrag
      top="5vh"
      title="Danh sách đối tượng xem hành trình"
      :visible.sync="isListVesselVoyageDialog"
      width="100%"
      :close-on-click-modal="false"
    >
      <div class="content-list-vessel-voyage" v-if="isShowListVesselDetail">
        <div
          v-for="(item, key, index) of voyage.listVesselToVoyage"
          v-bind:key="key"
          class="vessel-wrapper-detail"
        >
          <b-button
            @click="
              () => {
                collaseVesselClick(item);
              }
            "
            class="button-collapse"
            >Đối tượng: {{ item.name }} ID: {{ item.objId }}</b-button
          >
          <b-collapse
            :id="'collapse-' + item.objId"
            v-model="item.show"
            class="mt-2"
            style="margin-bottom: 4px"
          >
            <div id="popup-content">
              <b-row>
                <b-col sm="4" style="text-align: center">
                  <img
                    :src="item.imagePath == null ? '' : item.imagePath"
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
                      <span>{{ item.countryName }}</span>
                    </span>
                  </b-row>
                  <b-row>
                    <span
                      ><b>Tên đối tượng: </b>
                      <span>{{
                        item.vesselName ? item.vesselName : item.name
                      }}</span>
                    </span>
                  </b-row>
                  <!-- <b-row>
                    <span
                      ><b>Trạng thái: </b>
                      <span
                        :style="[
                          item.status == 1
                            ? { color: '#28a12c' }
                            : { color: 'red' },
                        ]"
                        ><b>{{
                          item.status == 1
                            ? "Đang hoạt động"
                            : "Không hoạt động"
                        }}</b></span
                      >
                    </span>
                  </b-row> -->
                </b-col>
              </b-row>
              <b-tabs content-class="mt-3">
                <b-tab title="Thông tin chung" active>
                  <div v-if="item.isUfo == 0">
                    <b-row>
                      <b-col sm="12" class="table-detail text-des">
                        <span
                          ><b>Loại tàu: </b>
                          <span
                            id="vesselType"
                            v-tooltip="{
                              content: item.vesselTypeName,
                            }"
                            >{{ item.vesselTypeName }}</span
                          >
                        </span>
                      </b-col>
                    </b-row>

                    <b-row>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>Dài: </b>
                          <span>{{ item.length }}</span>
                        </span>
                      </b-col>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>rộng: </b>
                          <span id="vesselType">{{ item.width }}</span>
                        </span>
                      </b-col>
                    </b-row>
                    <b-row>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>Mớm nước: </b>
                          <span>{{ item.draugth }} (m)</span>
                        </span>
                      </b-col>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>Hô hiệu: </b>
                          <span id="vesselType">{{ item.callSign }}</span>
                        </span>
                      </b-col>
                    </b-row>
                    <b-row>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>Tốc độ: </b>
                          <span>{{ item.sog }} (knot)</span>
                        </span>
                      </b-col>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>Hướng: </b>
                          <span id="vesselType">{{ item.cog }} (°)</span>
                        </span>
                      </b-col>
                    </b-row>
                    <b-row>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>Vĩ độ: </b>
                          <span>{{ item.latitude }}</span>
                        </span>
                      </b-col>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>Kinh độ: </b>
                          <span id="vesselType">{{ item.longitude }}</span>
                        </span>
                      </b-col>
                    </b-row>
                    <b-row>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>IP Nguồn: </b>
                          <span>{{ item.sourceIp }}</span>
                        </span>
                      </b-col>
                      <b-col sm="6" class="table-detail text-des">
                        <span
                          ><b>IP Đích: </b>
                          <span id="vesselType">{{ item.destIp }}</span>
                        </span>
                      </b-col>
                    </b-row>
                    <b-row>
                      <b-col sm="12" class="table-detail text-des">
                        <span
                          ><b>Chiều dữ liệu: </b>
                          <span>{{
                            item.direction == 1
                              ? "Chiều đi"
                              : item.direction == 2
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
                          <span>{{ item.ingestTime | formatDate }}</span>
                        </span>
                      </b-col>
                    </b-row>
                    <br />
                    <b-row>
                      <b-col sm="6">
                        <el-button
                          type="primary"
                          size="small"
                          style="width: 100%"
                          @click="handleGetVoyageDetail(item)"
                          >Xem hành trình</el-button
                        >
                      </b-col>
                      <b-col sm="6">
                        <el-button
                          type="primary"
                          size="small"
                          style="width: 100%"
                          @click="redirectToDetailVessel(item)"
                          >Thông tin chi tiết</el-button
                        >
                      </b-col>
                    </b-row>
                    <b-row style="margin-top: 6px">
                      <b-col sm="6">
                        <el-button
                          type="primary"
                          size="small"
                          style="width: 100%"
                          @click="openDialogAddToGroup(item)"
                          >Thêm vào nhóm</el-button
                        >
                      </b-col>

                      <b-col sm="6">
                        <el-button
                          type="primary"
                          size="small"
                          style="width: 100%"
                          @click="zoomHanhTrinh(item)"
                          >Zoom hành trình</el-button
                        >
                      </b-col>
                    </b-row>
                  </div>
                  <div v-if="item.isUfo == 1">
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
                          <span>{{ item.latitude }}</span>
                        </span>
                      </b-col>
                      <b-col sm="6" class="text-des table-detail">
                        <span
                          ><b>Kinh độ: </b>
                          <span id="vesselType">{{ item.longitude }}</span>
                        </span>
                      </b-col>
                    </b-row>
                    <b-row>
                      <b-col sm="6" class="text-des table-detail">
                        <span
                          ><b>Ip Nguồn: </b>
                          <span>{{ item.sourceIp }}</span>
                        </span>
                      </b-col>
                      <b-col sm="6" class="text-des table-detail">
                        <span
                          ><b>Ip Đích: </b>
                          <span id="vesselType">{{ item.destIp }}</span>
                        </span>
                      </b-col>
                    </b-row>
                    <b-row>
                      <b-col sm="12" class="text-des table-detail">
                        <span
                          ><b>Chiều dữ liệu: </b>
                          <span>{{
                            item.direction == 1
                              ? "Chiều đi"
                              : item.direction == 2
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
                          <span>{{ item.ingestTime | formatDate }}</span>
                        </span>
                      </b-col>
                    </b-row>
                    <br />
                    <b-row>
                      <b-col sm="6">
                        <el-button
                          type="primary"
                          size="small"
                          style="width: 100%"
                          @click="handleGetVoyageDetail(item)"
                          >Xem hành trình</el-button
                        >
                      </b-col>
                      <b-col sm="6">
                        <el-button
                          type="primary"
                          size="small"
                          style="width: 100%"
                          @click="openDialogAddToGroup(item)"
                          >Thêm vào nhóm</el-button
                        >
                      </b-col>
                    </b-row>
                    <b-row style="margin-top: 6px">
                      <b-col sm="6">
                        <el-button
                          type="primary"
                          size="small"
                          style="width: 100%"
                          @click="zoomHanhTrinh(item)"
                          >Zoom hành trình</el-button
                        >
                      </b-col>
                      <b-row> </b-row>
                    </b-row>
                  </div>
                </b-tab>

                <b-tab
                  title="Lịch sử hoạt động"
                  @click="historyTabActive(item)"
                >
                  <b-row>
                    <b-col
                      ><div class="info-history">
                        <div class="icon-info">
                          <i
                            class="el-icon-video-play"
                            style="font-size: 55px"
                          ></i>
                        </div>
                        <span>Thông tin media</span><br />
                        <span>{{ totalInfoMedia }}</span
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
                      ><div class="info-history">
                        <div class="icon-info">
                          <i
                            class="el-icon-warning-outline"
                            style="font-size: 55px"
                          ></i>
                        </div>
                        <span>Thông tin sự kiện</span><br />
                        <span>{{ totalInfoEvent }}</span
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
                </b-tab>
                <b-tab title="Ghi chú" @click="noteTabActive(item)">
                  <div class="note-list">
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
                      style="
                        width: 77%;
                        margin: 5px;
                        resize: none;
                        border: 1px solid #cecece;
                      "
                      placeholder="Nhập văn bản"
                      v-model="vesselDetailDialogNote.noteSend"
                    ></textarea>
                    <el-button
                      style="width: 70px; margin: 5px"
                      @click="addComment()"
                    >
                      Lưu
                    </el-button>
                  </b-row>
                </b-tab>
              </b-tabs>
            </div>
          </b-collapse>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          v-if="voyage.listVesselToVoyage.length > 1"
          @click="drawLineStringGroup()"
          >Xem hành trình tất cả đối tượng</el-button
        >
        <el-button @click="closeListVesselVoyageDialog()">Thoát</el-button>
        <!-- <el-button type="primary" @click="handleDrawVung()">Tạo vùng</el-button> -->
      </div>
    </el-dialog>
    <!-- chi tiết hành trình -->
    <el-dialog
      id="dl-VoyageDetailDialog"
      v-dialogDrag
      top="5vh"
      title="Chi tiết hành trình"
      :visible.sync="isVoyageDetailDialog"
      width="100%"
      :close-on-click-modal="false"
    >
      <div class="content-voyage-detail">
        <div
          v-for="(item, key, index) of voyage.lastesVoyageList"
          v-bind:key="key"
          class="vessel-wrapper-detail"
          v-bind:style="[
            selectVoyagePoint == item ? { 'background-color': '#cecece' } : {},
          ]"
          @click="detailVoyageVessel(item)"
        >
          <!-- key: {{ key }}, val: {{ item }}, index: {{ index }} -->
          <div class="line-point-vessel">
            <b-row>
              <b-col sm="5" md="5">
                <div class="lb-des-point-vessel">
                  {{ item.eventTime | formatDate }}
                </div>
              </b-col>
              <b-col>
                <div class="lb-detail-point-vessel">
                  Tốc độ: {{ item.sog }} (knot)
                </div>
              </b-col>
            </b-row>
            <b-row style="margin-top: -17px">
              <b-col sm="5" md="5"> </b-col>
              <b-col>
                <div class="lb-detail-point-vessel">
                  Hướng: {{ item.cog }} (°)
                </div>
              </b-col>
            </b-row>
            <div
              class="point-vessel-voyage"
              @click="detailVoyageVessel(item)"
            ></div>
          </div>
          <div
            v-if="key != Object.keys(voyage.lastesVoyageList).length - 1"
            class="line-vessel-voyage"
          ></div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isVoyageDetailDialog = false">Thoát</el-button>
        <!-- <el-button type="primary" @click="handleDrawVung()">Tạo vùng</el-button> -->
      </div>
    </el-dialog>
    <!-- thêm vào nhóm -->
    <el-dialog
      id="dialog-addToGroup"
      v-dialogDrag
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
    <!-- thông tin media dialog -->
    <el-dialog
      top="5vh"
      id="dl-media-info"
      v-dialogDrag
      :title="
        'Media của đối tượng có ID: ' +
        (this.selectedVesselToDetail ? this.selectedVesselToDetail.objId : '')
      "
      :visible.sync="isShowMediaInfoDialog"
      width="100%"
      :close-on-click-modal="false"
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
    <!-- playback dialog -->
    <!-- @close="stopPlaybackVoyage()" -->
    <el-dialog
      v-dialogDrag
      top="5vh"
      style=""
      id="playback-dialog"
      title="Playback hành trình"
      @close="cbClosePlayback()"
      :visible.sync="isShoyPlaybackDialog"
      width="100%"
      :close-on-click-modal="false"
    >
      <div class="con-playback">
        <div class="button-playback">
          <el-button v-if="!runningPlayback" @click="startAnimation()"
            >Start</el-button
          >

          <el-button v-if="runningPlayback" @click="stopAnimation()"
            >Stop</el-button
          >
        </div>
        <div class="bar-playback-parrent">
          <div class="bar-playback">
            <div class="track">
              <div class="knob"></div>
            </div>
          </div>
        </div>
        <div class="button-playback">
          <el-select
            id="sltTypeObject"
            collapse-tags
            v-model="speed"
            clearable
            style="width: 90%; margin-left: 20px"
            placeholder="Tốc độ"
            size="small"
          >
            <el-option disabled key="0" label="Tốc độ" value="null"></el-option>
            <el-option key="1" label="60" value="60"></el-option>
            <el-option key="2" label="80" value="80"></el-option>
            <el-option key="3" label="100" value="100"></el-option>
            <el-option key="4" label="200" value="200"></el-option>
          </el-select>
        </div>
      </div>
      <div slot="footer" class="dialog-footer" style="display: none">
        <!-- <el-button @click="closeMediaInfoDialog()">Đóng</el-button> -->
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      top="5vh"
      style=""
      class="el-bottom-search"
      id="search-by-group-dialog"
      title="Tìm kiếm theo nhóm đối tượng"
      :visible.sync="isShowDialogSearchlistByGroup"
      @close="stopPlaybackVoyage()"
      width="100%"
      :close-on-click-modal="false"
    >
      <b-row class="row">
        <b-col style="padding: 5px">
          <div class="label-form" style="width: 136px">
            <label>Từ ngày <span class="require">(*)</span></label>
          </div>
          <el-date-picker
            id="jack"
            v-model="query.startTime"
            size="small"
            format="dd/MM/yyyy HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            :editable="false"
            style="width: 297px; margin-left: 10px"
            type="datetime"
            placeholder="Từ ngày"
          ></el-date-picker>
        </b-col>
        <b-col style="padding: 5px">
          <div class="label-form" style="width: 136px">
            <label>Đến ngày <span class="require">(*)</span></label>
          </div>
          <el-date-picker
            v-model="query.endTime"
            size="small"
            format="dd/MM/yyyy HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            :editable="false"
            style="width: 297px; margin-left: 10px"
            type="datetime"
            placeholder="Đến ngày"
          ></el-date-picker>
        </b-col>
        <b-col style="padding: 5px">
          <div class="label-form" style="width: 136px">
            <label>Nhóm đối tượng <span class="require">(*)</span></label>
          </div>
          <el-select
            v-model="query.vesselGroup"
            value-key="id"
            placeholder="Nhóm đối tượng"
            @change="handleChangeVesselGroupSelect"
            style="width: 297px; margin-left: 10px"
          >
            <el-option
              :key="-1"
              :label="'Nhóm đối tượng'"
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
      <div slot="footer" class="dialog-footer search-footer">
        <el-button @click="handleClutersMake(null, null, true)"
          >tìm kiếm</el-button
        >
        <el-button @click="closeDialogLoadListByGroup()">Bỏ qua</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Map from "ol/Map.js";
import View from "ol/View.js";

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
import { getArea, getLength } from "ol/sphere";
import { unByKey } from "ol/Observable";
import Feature from "ol/Feature";
import Circle from "ol/geom/Circle";
import { Cluster, Stamen } from "ol/source";
import OverallApi from "../api/overall/api";
import { toStringHDMS } from "ol/coordinate";
import { defaults as defaultControls } from "ol/control";
import $ from "jquery";
import Overlay from "ol/Overlay";
import { getVectorContext } from "ol/render";
const dataSourceResource = new Resource("manager/source/get-all");
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
let source;
let currentDraw;

let today = new Date();
const startDate = new Date(today);

startDate.setDate(startDate.getDate() - 5);

let day = checkZero(today.getDate() + "");
let month = checkZero(today.getMonth() + 1 + "");

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

var animating = false;

var startTime;
var route;
var routeFeature;
var styles;
var geoMarker;
var startMarker;
var endMarker;
var vectorLayerPlayback;
var centerPlayback;
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
  //           "http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png",
  //         anchor: [0.5, 0.5],

  //         scale: 1.2,
  //         rotateWithView: true,
  //         rotation: -rotation,
  //       }),
  //     })
  //   );
  //   styles.push(
  //     new Style({
  //       geometry: new Point(start),
  //       image: new Icon({
  //         src:
  //           "http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png",
  //         anchor: [0.5, 0.5],

  //         scale: 1.2,
  //         rotateWithView: true,
  //         rotation: rotation,
  //       }),
  //     })
  //   );
  // });

  return styles;
};

function getObjectIcon(data, isMaster, statusId) {
  console.log("getObjectIcon 0 : ", data);
  var result = require("@/assets/icon/object/Yacht.png");
  if (data && data.typeId) {
    var vesselTypeId = data.typeId;

    if (isMaster == 1) {
      result = require("@/assets/icon/object/Master.png");
    } else if (statusId == 1 || statusId == 5) {
      result = require("@/assets/icon/object/AnchorMoored.png");
    } else {
      console.log("getObjectIcon: ", vesselTypeId);
      if (vesselTypeId >= 40 && vesselTypeId <= 49) {
        result = require("@/assets/icon/object/HightSpeedCraft.png");
      } else if (
        (vesselTypeId >= 50 && vesselTypeId <= 59) ||
        vesselTypeId == 10
      ) {
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
    src: getObjectIcon(feature.values_.data),
    anchor: [0.5, 0.5],
    scale: 1.3,
    rotateWithView: true,
    rotation: (feature.values_.data.cog * Math.PI) / 180,
  });
  return new Style({
    geometry: feature.getGeometry(),
    image: iconVessel,
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
    calculateClusterInfo(resolution);
    currentResolution = resolution;
  }
  var style;
  var size = feature.get("features").length;
  if (size > 1) {
    style = new Style({
      image: new CircleStyle({
        radius: feature.get("radius"),
        fill: new Fill({
          color: [255, 153, 0, Math.min(0.8, 0.4 + size / maxFeatureCount)],
        }),
      }),
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

var overLayPopup = null;
var feature_onClick;

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

var sourceMeasure = new VectorSource();

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
      isShowListObjectButton: false,
      isShowDialogSearchlistByGroup: false,
      speed: 60,
      runningPlayback: false,
      isShoyPlaybackDialog: false,
      currentHandleVoyage: [],
      isMeasureDientichMapDraw: false,
      isMeasureMapDraw: false,
      isShowDialogListAis: false,
      stepTabDetail: 0,
      selectVoyagePoint: null,

      vesselDetailDialogNote: {
        noteSend: "",
      },
      listComment: [],
      isShowListVesselDetail: true,
      isShowMediaInfoDialog: false,
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
      selectedVesselToDetail: null,
      addToGroup: {
        isShowDialogAddToGroup: false,
        data: {
          typeGroup: 1,
          newGroup: null,
          group: null,
        },
        listVesselGroup: null,
      },
      isListVesselVoyageDialog: false,
      isVoyageDetailDialog: false,
      urlParamQuery: null,
      isPlayback: null,
      arrVoyage: [],
      indexPlayback: [],
      intervalPlayback: null,
      translate: null,
      positionInfo: {},
      listAIS: [],
      listAISShow: [],
      loading: false,
      voyage: {
        isShowShipGrid: false,
        currentVoyageList: [],
        lastesVoyageList: null,
        listVesselToVoyage: [],
      },
      renderAddToGroupSelect: true,
      vesselGroup: {
        isShowvesselGroupGrid: false,
      },
      lstVesselFeature: [],
      areaQuery: {
        startTime: null,
        endTime: null,
        perPage: 10,
        currentPage: 1,
        rowsPerPage: 10,
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
        startTime:
          startDate.getFullYear() +
          "-" +
          checkZero(startDate.getMonth() + 1 + "") +
          "-" +
          checkZero(startDate.getDate() + "") +
          " 00:00:00",
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
      isShowVesselDetailDialog: true,
    };
  },
  created() {
    console.log("Created");
  },
  mounted() {
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
      // this.handleClutersMake();
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
        ],
        view: new View({
          projection: "EPSG:4326",
          center: hanoi,
          zoom: 1,
        }),
      });
      overLayPopup = new Overlay({
        element: document.getElementById("popup"),
        offset: [450, 0],
      });

      // map.on("click", async (evt) => {

      //   if (currentDraw != null) {
      //     return;
      //   }

      //   var element = overLayPopup.getElement();
      //   var coordinate = evt.coordinate;
      //   var hdms = toStringHDMS(toLonLat(coordinate));

      //   feature_onClick = map.forEachFeatureAtPixel(
      //     evt.pixel,
      //     function (feature, layer) {
      //       console.log(feature);
      //       return feature;
      //     }
      //   );

      //   if (feature_onClick) {
      //     // var content = document.getElementById("popup-content");
      //     // console.log(feature_onClick.getProperties().name);
      //     // overLayPopup.setPosition(evt.coordinate);
      //     overLayPopup.setPosition(coordinate);
      //     this.isShowVesselDetailDialog = true;
      //     if (
      //       feature_onClick.values_.features &&
      //       feature_onClick.values_.features.length == 1
      //     ) {
      //       var data = feature_onClick.values_.features[0].values_.data;
      //       console.log(data);

      //       const overallApi = new OverallApi();
      //       var res = await overallApi.getVesselDetail(
      //         "/ais/vessel/detail/" + data.objId
      //       );
      //       if (res.status == 200) {
      //         var vesselDetailInfo = res.data != null ? res.data : {};

      //         this.vesselDetailDialog = vesselDetailInfo;
      //       }
      //       this.vesselDetailDialog = { ...data, ...this.vesselDetailDialog };
      //       console.log("detail vessel ", this.vesselDetailDialog);
      //       // document.getElementById("popup-content").innerHTML =
      //       //   "Mã đối tượng " +
      //       //   data.objId +
      //       //   "</br>" +
      //       //   "Tên đối tượng " +
      //       //   vesselDetailInfo.vesselName +
      //       //   "</br>" +
      //       //   0;
      //       // "Kinh độ: " +
      //       //   data.longitude +
      //       //   "</br>" +
      //       //   "Vĩ độ " +
      //       //   data.latitude +
      //       //   "</br>";
      //     } else if (feature_onClick.values_.data) {
      //       var data = feature_onClick.values_.data;
      //       console.log(data);
      //       const overallApi = new OverallApi();
      //       var res = await overallApi.getVesselDetail(
      //         "/ais/vessel/detail/" + data.objId
      //       );
      //       if (res.status == 200) {
      //         var vesselDetailInfo = res.data != null ? res.data : {};
      //         console.log("detail vessel ", vesselDetailInfo);
      //         this.vesselDetailDialog = vesselDetailInfo;
      //       }
      //       this.vesselDetailDialog = { ...data, ...this.vesselDetailDialog };
      //       console.log("detail vessel ", this.vesselDetailDialog);
      //     } else if (
      //       feature_onClick.type == "area" ||
      //       feature_onClick.values_.type == "area"
      //     ) {
      //       var data = feature_onClick.data
      //         ? feature_onClick.data
      //         : feature_onClick.values_.dataVung;
      //       console.log(data);
      //       document.getElementById("popup-content").innerHTML =
      //         "vùng: " + data.name + "</br>";
      //     } else {
      //       overLayPopup.setPosition(null);
      //       this.isShowVesselDetailDialog = false;
      //       this.positionInfo.longitude = coordinate[0];
      //       this.positionInfo.latitude = coordinate[1];
      //     }
      //   } else {
      //     overLayPopup.setPosition(null);
      //     this.isShowVesselDetailDialog = false;
      //     this.positionInfo.longitude = coordinate[0];
      //     this.positionInfo.latitude = coordinate[1];
      //   }
      // });

      map.on("click", this.mapClick.bind(this));

      map.addOverlay(overLayPopup);

      console.log("param query: ", this.$route.query);
      this.urlParamQuery = this.$route.query;
      if (this.urlParamQuery.objId) {
        this.query.mmsi = this.urlParamQuery.objId;
        this.query.startTime = this.urlParamQuery.startTime;
        this.query.endTime = this.urlParamQuery.endTime;
        debugger;
        var typeObject = this.lstTypeObject.filter((val) => {
          console.log("isUfo: ", this.urlParamQuery);
          console.log("val: ", val);

          if (
            this.urlParamQuery.isUfo &&
            val.id == Number(this.urlParamQuery.isUfo)
          ) {
            return val;
          }
        });
        if (typeObject && typeObject.length > 0) {
          this.query.typeObject = typeObject[0].id;
        }
        console.log("typeObject: ", this.query.typeObject);

        this.urlParamQuery.isUfo;
        this.openDialogLoadListVessel();

        setTimeout(() => {
          this.handleClutersMake(null, () => {
            this.handleListVesselVoyageGroup(true);
            this.openListVesselVoyageDialog();
            this.closeDialogListVessel();
          });
        }, 100);
      }
    },
    async mapClick(evt) {
      console.log("map click", evt);
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
        debugger;
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
          if (data.isUfo == 1) {
            try {
              this.vesselDetailDialog = {...data};
              this.objectDetailDialog = {...data};
              if (data && typeof data.objId === "number") {
                return;
              }
              var res = await overallApi.postApi(
                "/contact/Object/detail/getDetailObjectInfo",
                {
                  uuid: data.objId,
                }
              );
              if (res.status == 200) {
                var objectDetailDialog = res.data != null ? res.data : {};

                this.objectDetailDialog = objectDetailDialog;
              }
              this.objectDetailDialog = { ...data, ...this.objectDetailDialog };
              console.log("detail un object ", this.objectDetailDialog);
            } catch (error) {
              // this.$notify({
              //   title: "Lấy thông tin đối tượng thất bại",
              //   position: "top right",
              //   type: "error",
              // });
              return;
            }
          } else if (data.isUfo == 0) {
            try {
              this.vesselDetailDialog = {};
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
              // this.$notify({
              //   title: "Lấy thông tin đối tượng thất bại",
              //   position: "top right",
              //   type: "error",
              // });
            }
          }
        } else if (feature_onClick.values_.data) {
          var data = feature_onClick.values_.data;
          console.log(data);
          const overallApi = new OverallApi();
          if (data.isUfo == 1) {
            try {
              this.vesselDetailDialog = { ...data};
              this.objectDetailDialog = { ...data};
              var res = await overallApi.postApi(
                "/contact/Object/detail/getDetailObjectInfo",
                {
                  uuid: data.objId,
                }
              );
              if (res.status == 200) {
                var objectDetailDialog = res.data != null ? res.data : {};

                this.objectDetailDialog = objectDetailDialog;
              }
              this.objectDetailDialog = { ...data, ...this.objectDetailDialog };
              console.log("detail un object ", this.objectDetailDialog);
            } catch (error) {
              // this.$notify({
              //   title: "Lấy thông tin đối tượng thất bại",
              //   position: "top right",
              //   type: "error",
              // });
              return;
            }
          } else if (data.isUfo == 0) {
            try {
              this.vesselDetailDialog = {};
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
              // this.$notify({
              //   title: "Lấy thông tin đối tượng thất bại",
              //   position: "top right",
              //   type: "error",
              // });
            }
          }
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

    detailVoyageVessel(data) {
      console.log("chi tiết đối tượng hành trình ", data);
      this.selectVoyagePoint = data;
      this.aisObjectDetail(data);
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

    setAltImg(event) {
      // console.log("image error");
      event.target.src = require("@/assets/images/vessel/vessel_default.jpg");
    },

    openVoyageDetailDialog() {
      this.isVoyageDetailDialog = true;
    },

    closeVoyageDetailDialog() {
      this.isVoyageDetailDialog = false;
    },

    async handleGetVoyageDetail(_data) {
      var data;
      if (_data) {
        data = _data;
        this.currentHandleVoyage = [data];
      } else {
        data =
          this.currentHandleVoyage && this.currentHandleVoyage.length > 0
            ? this.currentHandleVoyage[0]
            : null;
      }
      if (!data) {
        return;
      }
      this.openVoyageDetailDialog();
      this.resetCurrentVoyageList();
      this.clearAllFeature();
      this.closeDialogPlayback();
      console.log("GetVoyageDetail", data);
      const overallApi = new OverallApi();
      var resListAISDetail = await overallApi.searchListAis(
        "/ais/search-list",
        {
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          groupIds: this.query.vesselGroup ? [this.query.vesselGroup.id] : null,
          dataSource: this.query.dataSource,
          sourceIps: this.query.sourceIps
            ? this.query.sourceIps.trim()
            : this.query.sourceIps,
          destIps: this.query.destIps
            ? this.query.destIps.trim()
            : this.query.destIps,
          countryId: this.query.country ? this.query.country.id : null,
          typeId: this.query.vesselType ? this.query.vesselType.id : null,
          mmsi: data.objId,
          areaIds: this.query.area ? [this.query.area.id] : null,
          limit: 10000,
        }
      );

      console.log("result VoyageDetail", resListAISDetail);
      if (resListAISDetail.status == 200) {
        console.log("result VoyageDetail", resListAISDetail.data);
        // if (resListAISDetail.data && resListAISDetail.data.length < 2) {
        //   this.$notify({
        //     title: "Chỉ có 1 ví trí đối tượng được tìm thấy",
        //     position: "top right",
        //     type: "waring",
        //   });
        // }
        this.resetCurrentVoyageList();
        resListAISDetail.data.reverse();
        this.handleLineStringDraw(resListAISDetail.data);
        this.isPlayback = true;
        if (resListAISDetail.data.length == 0) {
          this.$notify({
            title: "Không có hành trình nào",
            position: "top right",
            type: "waring",
          });
          return;
        }
      } else {
        this.$notify({
          title: "Lấy hành trình thất bại",
          position: "top right",
          type: "error",
        });
        return;
      }
      if (resListAISDetail.data && resListAISDetail.data.length > 0) {
        this.centerToPoisition(
          resListAISDetail.data[0].longitude,
          resListAISDetail.data[0].latitude,
          8
        );
      }
      this.closeDialogSearchListVessel();
    },

    async handleListVesselVoyageDetail(data, isGroup, isOpen) {
      // this.closeDialogSearchListVessel();
      this.openListVesselVoyageDialog();
      this.closeVoyageDetailDialog();
      console.log("handleListVesselVoyageDetail: ", data);
      if (!isGroup) {
        this.voyage.listVesselToVoyage = [data];
        if (isOpen) {
          this.voyage.listVesselToVoyage.show = isOpen;
        }
      } else {
        this.voyage.listVesselToVoyage = data;
        if (isOpen && this.voyage.listVesselToVoyage.length > 0) {
          this.voyage.listVesselToVoyage[0].show = isOpen;
          this.selectVesselToDetail(this.voyage.listVesselToVoyage[0]);
          setTimeout(() => {
            this.handleGetVoyageDetail(this.selectedVesselToDetail);
          }, 1000);
        }
      }
      this.rerenderShowListVesselDetail();
    },

    playbackVoyage() {
      // this.isPlayback = false;

      this.clearAllFeature();
      this.intervalPlayback && clearInterval(this.intervalPlayback);
      console.log("playbackVoyage ", this.voyage.currentVoyageList);
      this.arrVoyage = [];
      var currentVoyageList = this.voyage.currentVoyageList;
      this.newPlayback(this.voyage.currentVoyageList[0]);
      this.openDialogPlayback();
      this.startAnimation();
      // for (var i = 0; i < currentVoyageList.length; i++) {
      //   var currentVoyage = currentVoyageList[i];
      //   this.arrVoyage[i] = currentVoyage;
      //   this.indexPlayback[this.arrVoyage[i]] = 0;
      //   console.log("vẽ line", currentVoyage);
      // }

      // this.intervalPlayback = setInterval(
      //   this.intervalDrawCallback.bind(this),
      //   1000
      // );
    },

    stopPlaybackVoyage() {
      this.stopAnimation(true);

      this.handleGetVoyageDetail();
      this.intervalPlayback && clearInterval(this.intervalPlayback);
      // this.isPlayback = !this.isPlayback;
      // currentVoyageList
    },

    cbClosePlayback() {
      this.isPlayback = true;
      this.stopAnimation(true);
      this.clearAllFeaturePlayback();
    },

    intervalDrawCallback() {
      var draw = false;

      for (var j = 0; j < this.arrVoyage.length; j++) {
        var index = this.indexPlayback[this.arrVoyage[j]];

        if (index < this.arrVoyage[j].length - 1) {
          console.log("point 1", index);
          console.log("point 2", index + 1);
          this.handleLineStringPlayback([
            this.arrVoyage[j][index],
            this.arrVoyage[j][index + 1],
          ]);
          draw = true;
        }
        this.indexPlayback[this.arrVoyage[j]] += 1;
      }

      if (draw == false) {
        console.log("end play back;");
        this.isPlayback = true;
        clearInterval(this.intervalPlayback);
      }
    },

    closeDialogSearchListVessel() {
      this.isShowDialog = false;
    },

    handleChangeVesselGroupSelect() {
      console.log("vessel group change");
    },

    //phân trang list nhóm đối tượng
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

    closeDialogAddToGroup() {
      this.addToGroup.isShowDialogAddToGroup = false;
    },

    async openDialogAddToGroup(data) {
      this.handleGetVesselGroup(1, 10000000);
      this.addToGroup.isShowDialogAddToGroup = true;
      this.addToGroup.data.group = null;
      this.addToGroup.data.newGroup = null;
      this.selectVesselToDetail(data);
    },

    collaseVesselClick(item) {
      // console.log("item show: ", item.show);
      var index = this.voyage.listVesselToVoyage.index;
      item.show = !item.show;
      this.rerenderShowListVesselDetail();

      this.selectVesselToDetail(item);
    },

    selectVesselToDetail(val) {
      this.selectedVesselToDetail = val;
    },

    deselectVesselToDetail() {
      this.selectedVesselToDetail = null;
    },

    async toAddToGroup(data) {
      if (this.addToGroup.data.typeGroup == 1) {
        console.log("addToGroup", this.selectedVesselToDetail);
        if (this.addToGroup.data.group == null) {
          this.$notify({
            title: "Nhóm không được để trống",
            position: "top right",
            type: "error",
          });
          return;
        }
        const overallApi = new OverallApi();
        console.log(
          "this.selectedVesselToDetail: ",
          this.selectedVesselToDetail
        );
        var resListAISDetail = await overallApi.searchListAis(
          "/ais/object/add-to-group",
          {
            objId: this.selectedVesselToDetail.objId,
            objectGroupId: this.addToGroup.data.group.id,
          }
        );

        console.log("result VoyageDetail", resListAISDetail);
        if (resListAISDetail.status == 200) {
          console.log("result VoyageDetail", resListAISDetail.data);
          this.$notify({
            title: "Thêm đối tượng vào nhóm thành công.",
            position: "top right",
            type: "success",
          });
          this.closeDialogAddToGroup();
        }
      } else if (this.addToGroup.data.typeGroup == 2) {
        console.log("addToGroup", this.addToGroup);
        console.log(
          "this.selectedVesselToDetail2: ",
          this.selectedVesselToDetail
        );
        const overallApi = new OverallApi();
        var resListAISDetail = await overallApi.searchListAis(
          "/ais/object/add-to-group",
          {
            objId: this.selectedVesselToDetail.objId,
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
            position: "top right",
            type: "success",
          });
          this.closeDialogAddToGroup();
        }
      }
    },

    handleChangeVesselGroupSelect() {
      console.log("vessel group change");
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

    async addComment() {
      if (
        !this.vesselDetailDialogNote.noteSend ||
        this.vesselDetailDialogNote.noteSend == null ||
        this.vesselDetailDialogNote.noteSend == ""
      ) {
        this.$notify({
          title: "Ghi chú không được để trống",
          position: "top right",
          type: "waring",
        });
        return;
      }
      const overallApi = new OverallApi();
      var res = await overallApi.getListComment(
        "/media/list/addCommentCommon",
        {
          commentTypeId: 3,
          refId: this.selectedVesselToDetail.objId,
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

    aisObjectDetail(data) {
      console.log("aisObjectDetail: ", data);
      // this.closeDialogSearchAisvessel();
      this.centerToPoisition(
        data.longitude,
        data.latitude,
        map.getView().getZoom() < 10
          ? map.getView().getZoom() + 8
          : map.getView().getZoom()
      );
      this.$nextTick(function () {
        setTimeout(() => {
          this.mapClick([data.longitude, data.latitude]);
        }, 200);
      });
    },

    redirectToDetailVessel(item) {
      console.log("redirect to vessel detal ", item);
      this.$router.push({
        path: "/contact/vessel-detail",
        query: { mmsi: item ? item.objId : "" },
      });
    },

    zoomHanhTrinh(data) {
      console.log("zoomHanhTrinh ", data);
      if (data) {
        this.centerToPoisition(
          data.longitude,
          data.latitude,
          map.getView().getZoom() + 2
        );
      } else {
        this.$notify({
          title: "Không có hành trình đối tượng",
          position: "top right",
          type: "error",
        });
      }
    },

    rerenderShowListVesselDetail() {
      this.isShowListVesselDetail = false;
      this.$nextTick(() => {
        this.isShowListVesselDetail = true;
      });
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
            //.post(`${host}/media/fetch-m3u8-file`, payLoad)
            .post(
              `http://192.168.61.106:8415/v1.0/media/fetch-m3u8-file`,
              payLoad
            )
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
      // console.log("measure length: ", length);
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

    // new playback

    newPlayback(listPlayback) {
      debugger;
      if (listPlayback && listPlayback.length > 0) {
        route = [];
        routeFeature = [];
        geoMarker = [];
        startMarker = [];
        endMarker = [];
        vectorLayerPlayback = [];
        for (var i = 0; i < listPlayback.length; i++) {
          var data = listPlayback[i];
          var locations = [];
          for (var j = 0; j < data.length; j++) {
            locations.push([data[j].longitude, data[j].latitude]);
          }

          // OpenLayers uses [lon, lat], not [lat, lon]
          // locations.map(function (l) {
          //   return l.reverse();
          // });

          route[i] = new LineString(locations);

          var routeCoords = route[i].getCoordinates();
          var routeLength = routeCoords.length;

          routeFeature[i] = new Feature({
            type: "route",
            data: data,
            isPlayback: true,
            geometry: route[i],
          });
          geoMarker[i] = new Feature({
            type: "geoMarker",
            data: data[0],
            isPlayback: true,
            geometry: new Point(routeCoords[0]),
          });
          startMarker[i] = new Feature({
            type: "icon",
            data: data[0],
            isPlayback: true,
            geometry: new Point(routeCoords[0]),
          });
          endMarker[i] = new Feature({
            type: "icon",
            data: data[routeLength - 1],
            isPlayback: true,
            geometry: new Point(routeCoords[routeLength - 1]),
          });
          styles = {
            route: new Style({
              stroke: new Stroke({
                width: 6,
                color: [237, 212, 0, 0.8],
              }),
            }),
            // icon: new Style({
            //   image: new Icon({
            //     anchor: [0.5, 0.5],
            //     scale: 1.2,
            //     src:
            //       "http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png",
            //     // rotation: (feature.values_.data.cog * Math.PI) / 180,
            //     // rotateWithView: true,
            //   }),
            // }),
            // geoMarker: new Style({
            //   image: new Icon({
            //     anchor: [0.5, 0.5],
            //     scale: 1.2,
            //     src:
            //       "http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png",
            //     // rotation: (feature.values_.data.cog * Math.PI) / 180,
            //     // rotateWithView: true,
            //   }),
            //   // new CircleStyle({
            //   //   radius: 7,
            //   //   fill: new Fill({ color: "black" }),
            //   //   stroke: new Stroke({
            //   //     color: "red",
            //   //     width: 2,
            //   //   }),
            //   // }),
            // }),
          };

          vectorLayerPlayback[i] = new VectorLayer({
            isPlayback: true,
            source: new VectorSource({
              features: [
                routeFeature[i],
                geoMarker[i],
                startMarker[i],
                endMarker[i],
              ],
            }),
            style: (feature) => {
              // hide geoMarker if animation is active
              if (animating && feature.get("type") === "geoMarker") {
                return null;
              } else if (feature.get("type") === "geoMarker") {
                console.log(feature);
                return new Style({
                  image: new Icon({
                    anchor: [0.5, 0.5],
                    scale: 1.2,
                    src: getObjectIcon(feature.values_.data),
                    rotation: (feature.values_.data.cog * Math.PI) / 180,
                    rotateWithView: true,
                  }),
                });
              } else if (feature.get("type") === "icon") {
                return new Style({
                  image: new Icon({
                    anchor: [0.5, 0.5],
                    scale: 1.2,
                    src: getObjectIcon(feature.values_.data),
                    rotation: (feature.values_.data.cog * Math.PI) / 180,
                    rotateWithView: true,
                  }),
                });
              } else {
                return styles[feature.get("type")];
              }
            },
          });
          map.addLayer(vectorLayerPlayback[i]);
        }
      }
      if (listPlayback && listPlayback.length == 1) {
        map
          .getView()
          .fit(vectorLayerPlayback[0].getSource().getExtent(), map.getSize(), {
            padding: [5, 5, 5, 5],
          });
        map.getView().setZoom(map.getView().getZoom() - 1);
      }
      centerPlayback = map.getView().getCenter();
    },

    moveFeature(event) {
      for (var j = 0; j < route.length; j++) {
        var vectorContext = getVectorContext(event);
        var frameState = event.frameState;

        if (animating) {
          var elapsedTime = frameState.time - startTime;
          var distance = (this.speed * elapsedTime) / 1e6;
          console.log("moving.. ", distance);
          $(".knob").css("left", distance * 100 + "%");
          if (distance >= 1) {
            this.stopAnimation(true);
            return;
          }

          var currentPoint = new Point(route[j].getCoordinateAt(distance));
          var feature = new Feature(currentPoint);
          vectorContext.drawFeature(
            feature,
            new Style({
              image: new CircleStyle({
                radius: 7,
                fill: new Fill({ color: "black" }),
                stroke: new Stroke({
                  color: "red",
                  width: 2,
                }),
              }),
            })
          );
        }
      }
      // tell OpenLayers to continue the postrender animation
      map.render();
    },

    startAnimation() {
      if (animating) {
        this.stopAnimation(false);
      } else {
        for (var i = 0; i < geoMarker.length; i++) {
          animating = true;
          this.runningPlayback = true;
          startTime = new Date().getTime();
          // startButton.textContent = "Cancel Animation";
          // hide geoMarker
          geoMarker[i].changed();
          // just in case you pan somewhere else
          if (geoMarker.length == 1) {
            map.getView().setCenter(centerPlayback);
          }
          vectorLayerPlayback[i].on("postrender", this.moveFeature.bind(this));
          map.render();
        }
      }
    },

    stopAnimation(ended) {
      animating = false;
      this.runningPlayback = false;
      // this.isPlayback = !this.isPlayback;
      // startButton.textContent = "Start Animation";
      for (var i = 0; i < geoMarker.length; i++) {
        // if animation cancelled set the marker at the beginning
        var coord = route[i].getCoordinateAt(ended ? 1 : 0);
        geoMarker[i].getGeometry().setCoordinates(coord);
        // remove listener
        vectorLayerPlayback[i].un("postrender", this.moveFeature.bind(this));
      }
    },
    // end new Playback

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
      this.cancelMeasureMap();
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

    cancelMeasureMap() {
      this.isMeasureMapDraw = false;
      this.isMeasureDientichMapDraw = false;
      this.clearAllFeatureDrawMeasure();
      map
        .getOverlays()
        .getArray()
        .slice(0)
        .forEach(function (overlay) {
          // console.log("remove ol ", overlay);
          if (overlay.id && overlay.id.isMeasure) {
            map.removeOverlay(overlay);
            // console.log("remove ol del ", overlay);
          }
        });
    },

    async detailMedia() {
      this.openMediaInfoDialog();
      const overallApi = new OverallApi();

      // Standard
      let payLoad = {
        startTime: this.query.startTime,
        endTime: this.query.endTime,
        objId:
          this.selectedVesselToDetail.isUfo == 0
            ? this.selectedVesselToDetail.objId
            : this.objectDetailDialog.objId,
        currentPage: this.mediaInfo.query.currentPage,
        rowsPerPage: this.mediaInfo.query.rowsPerPage,
      };

      // Video
      // let payLoad = {
      //   startTime: "2021-03-02 07:05:24", //this.query.startTime,
      //   endTime: "2021-03-04 07:05:24", //this.query.endTime,
      //   objId: "412333612", // this.vesselDetailDialog.objId,
      //   currentPage: this.mediaInfo.query.currentPage,
      //   rowsPerPage: this.mediaInfo.query.rowsPerPage,
      // };

      // Audio
      // let payLoad = {
      //   startTime: "2021-04-02 00:00:00",
      //   endTime: "2021-04-02 09:00:00",
      //   objId: "412333613", // 412333614
      //   currentPage: this.mediaInfo.query.currentPage,
      //   rowsPerPage: this.mediaInfo.query.rowsPerPage,
      // };

      // Web
      // let payLoad = {
      //   startTime: "2021-04-12 00:00:00",
      //   endTime: "2021-04-12 09:00:00",
      //   objId: "148", // 413454580
      //   currentPage: this.mediaInfo.query.currentPage,
      //   rowsPerPage: this.mediaInfo.query.rowsPerPage,
      // };

      // Email
      // let payLoad = {
      //   startTime: "2021-04-02 00:00:00",
      //   endTime: "2021-04-02 09:00:00",
      //   objId: "413493740",
      //   currentPage: this.mediaInfo.query.currentPage,
      //   rowsPerPage: this.mediaInfo.query.rowsPerPage,
      // };

      // Undefined
      // let payLoad = {
      //   startTime: "2021-04-12 00:00:00",
      //   endTime: "2021-04-12 09:00:00",
      //   objId: "412477720", // 412002304
      //   currentPage: this.mediaInfo.query.currentPage,
      //   rowsPerPage: this.mediaInfo.query.rowsPerPage,
      // };

      console.log("payLoad: ", payLoad);

      let res = await overallApi.postApi("/media/list/search", payLoad);
      console.log("detail media: ", res);

      if (res.status == 200) {
        this.mediaInfo.lstMedia = res.data;
        console.log("chi tiết thông tin media", this.vesselDetailInfo);
      } else {
        this.$notify({
          title: "Lấy thông tin list media thất bại",
          position: "top right",
          type: "error",
        });
      }
      // alert("chi tiết media");
    },

    openMediaInfoDialog() {
      this.isShowMediaInfoDialog = true;
      this.mediaInfo.query.currentPage = 1;
      this.mediaInfo.query.rowsPerPage = 30;
    },

    closeMediaInfoDialog() {
      this.isShowMediaInfoDialog = false;
    },

    async historyTabActive(data) {
      this.selectVesselToDetail(data);
      const overallApi = new OverallApi();
      console.log("chi tiết thông tin media Object id: ", data);
      // console.log("chi tiết thông tin media objectDetailDialog: ", this.objectDetailDialog);
      try {
        var res = await overallApi.getVesselDetail("/media/total-by-object", {
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          objId: data.isUfo == 0 ? data.objId : this.objectDetailDialog.objId,
        });
        console.log("/media/total-by-object: ", res);
        if (res.status == 200) {
          this.totalInfoMedia = res.total;
        }
      } catch (error) {
        this.totalInfoMedia = 0;
      }
    },

    noteTabActive(data) {
      this.selectVesselToDetail(data);
      this.loadListComment();
    },

    async loadListComment() {
      console.log("loadListComment: ", this.selectedVesselToDetail);
      const overallApi = new OverallApi();
      var res = await overallApi.getListComment(
        "/media/list-comments",
        {
          refId: this.selectedVesselToDetail.objId,
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
        if (res.status == 200 && (res.message == null || res.message == "")) {
          this.$notify({
            title: "Lưu vùng thành công",
            position: "top right",
            type: "success",
          });
          currentDraw = null;
          this.removeSelect();
        } else {
          this.$notify({
            title: "Lưu vùng thất bại",
            message: res.message,
            position: "top right",
            type: "error",
          });
        }
      } catch (error) {
        this.$notify({
          title: "Lưu vùng thất bại",
          position: "top right",
          type: "error",
        });
        console.log("save  draw error ", error);
      }
    },

    editArea() {
      this.isShowVung = true;
      this.vung.isUpdate = true;
      console.log("edit area ", this.selectedFeature);
      this.vung = this.selectedFeature;
    },

    addArea() {
      this.vung.isUpdate = false;
      this.vung.name = "";
      this.isShowVung = true;
    },

    toggleDialogPlayback() {
      console.log("toggleDialogPlayback..", this.isShoyPlaybackDialog);
      this.$nextTick(function () {
        setTimeout(() => {
          this.isShoyPlaybackDialog = !this.isShoyPlaybackDialog;
          console.log("isShoyPlaybackDialog..", this.isShoyPlaybackDialog);
          if (this.isShoyPlaybackDialog) {
            this.newPlayback(this.voyage.currentVoyageList);
          } else {
            this.stopAnimation(true);
            this.clearAllFeaturePlayback();
          }
        }, 0);
      });

      this.isPlayback = !this.isPlayback;
    },

    playbackGroup() {
      this.newPlayback(this.voyage.currentVoyageList);
    },

    openDialogPlayback() {
      this.isShoyPlaybackDialog = true;
    },

    closeDialogPlayback() {
      this.isShoyPlaybackDialog = false;
    },

    openDialogLoadListByGroup() {
      this.isShowDialogSearchlistByGroup = true;
    },

    closeDialogLoadListByGroup() {
      this.isShowDialogSearchlistByGroup = false;
    },

    openDialogLoadListVessel() {
      this.isShowDialog = true;
    },

    openDialogListVessel() {
      this.isShowDialogListAis = true;
    },

    closeDialogListVessel() {
      this.isShowDialogListAis = false;
    },

    resetCurrentVoyageList() {
      this.voyage.currentVoyageList = [];
      this.voyage.lastesVoyageList = [];
      console.log("resetCurrentVoyageList ", this.voyage.currentVoyageList);
    },

    async drawLineStringGroup() {
      this.resetCurrentVoyageList();
      this.clearAllFeature();

      var data = [];
      var overallApi = new OverallApi();
      for (var i = 0; i < this.voyage.listVesselToVoyage.length; i++) {
        var res = await overallApi.searchListAis("/ais/search-list", {
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          groupIds: this.query.vesselGroup ? [this.query.vesselGroup.id] : null,
          dataSource: this.query.dataSource,
          sourceIps: this.query.sourceIps,
          destIps: this.query.destIps,
          countryId: this.query.country ? this.query.country.id : null,
          typeId: this.query.vesselType ? this.query.vesselType.id : null,
          mmsi: this.voyage.listVesselToVoyage[i].objId,
          areaIds: this.query.area ? [this.query.area.id] : null,

          limit: 500000,
        });
        if (res.status == 200) {
          // this.$notify({
          //   message: "Tìm thấy " + res.data.length + " Vị trí đối tượng.",
          //   position: "top right",
          //   type: "success",
          // });
          data.push(res.data);
          // console.log("data get: ", data);
        }
      }

      // if (res.status == 200) {
      //   // this.$notify({
      //   //   message: "Tìm thấy " + res.data.length + " Vị trí đối tượng.",
      //   //   position: "top right",
      //   //   type: "success",
      //   // });

      //   data.push(res.data);
      // }
      // else{
      //   this.$notify({
      //     message: "Lấy danh sách vị trí thất bại",
      //     position: "top right",
      //     type: "error",
      //   });
      //   return;
      // }
      if (data && data.length > 0) {
        if (data[0] && data[0].length > 0) {
          this.centerToPoisition(data[0][0].longitude, data[0][0].latitude, 5);
        }

        console.log("data draw: ", data);
        if (data.length > 2000) {
          this.$notify({
            title: "Vượt quá số lượng cho phép vẽ hành trình",
            position: "top right",
            type: "error",
          });
          return;
        }
        this.isPlayback = true;
        for (var i = 0; i < data.length; i++) {
          var _itemI = data[i];
          _itemI.reverse();
          console.log("draw line group: ", _itemI);
          if (_itemI.length >= 2) {
            this.handleLineStringDraw(_itemI);
          }
        }
      }
    },

    handleLineStringPlayback(data) {
      console.log("playback: ", data);
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
            label: "đối tượng",
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
              scale: 1.2,
              src: getObjectIcon(feature.values_.data),
              rotation: (feature.values_.data.cog * Math.PI) / 180,
              rotateWithView: true,
            }),
          });
        },
      });

      map.addLayer(vectorLayer);
      map.addLayer(layerPointVessel);
    },

    handleLineStringDraw(data) {
      var _dataClone = [...data];
      // _dataClone.reverse();
      console.log("linestring ", _dataClone);
      this.voyage.lastesVoyageList = data;
      this.voyage.currentVoyageList.unshift(_dataClone);
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
            label: "đối tượng",
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
          // console.log(
          //   "hướng: ",
          //   ((feature.values_.data.cog - 90) * Math.PI) / 180
          // );
          return new Style({
            image: new Icon({
              anchor: [0.5, 0.5],
              scale: 1.2,
              src: getObjectIcon(feature.values_.data),
              rotation: (feature.values_.data.cog * Math.PI) / 180,
              rotateWithView: true,
            }),
          });
        },
      });

      map.addLayer(vectorLayer);
      map.addLayer(layerPointVessel);
    },

    async handleListVesselVoyageGroup(isShow) {
      // this.listAIS
      var listAISShow = this.listAIS.slice(0, 20);
      console.log("handleListVesselVoyageGroup: ", listAISShow);
      this.handleListVesselVoyageDetail(listAISShow, true, isShow);
    },

    async handleClutersMake(drawLineStringGroup, callback, onlyGroup) {
      this.isShowListObjectButton = false;
      this.clearAllFeature();
      const overallApi = new OverallApi();
      var listAIS;
      try {
        console.log(this.query);
        if (this.query.startTime == null || this.query.endTime == null) {
          this.$notify({
            title: "Lỗi",
            message: "Thời gian là bắt buộc",
            position: "top right",
            type: "error",
          });
          return;
        }
        if (onlyGroup) {
          if (!this.query.vesselGroup) {
            this.$notify({
              title: "Lỗi",
              message: "Nhóm đối tượng là bắt buộc",
              type: "error",
            });
            return;
          }
          listAIS = await overallApi.searchListAis("/ais/search-list-general", {
            startTime: this.query.startTime,
            endTime: this.query.endTime,
            groupIds: this.query.vesselGroup
              ? [this.query.vesselGroup.id]
              : null,
            limit: 500000,
          });
        } else {
          listAIS = await overallApi.searchListAis("/ais/search-list-general", {
            startTime: this.query.startTime,
            endTime: this.query.endTime,
            groupIds: this.query.vesselGroup
              ? [this.query.vesselGroup.id]
              : null,
            dataSource: this.query.dataSource,
            sourceIps: this.query.sourceIps
              ? this.query.sourceIps.trim()
              : this.query.sourceIps,
            destIps: this.query.destIps
              ? this.query.destIps.trim()
              : this.query.destIps,
            countryId: this.query.country ? this.query.country.id : null,
            typeId: this.query.vesselType ? this.query.vesselType.id : null,
            mmsi: this.query.mmsi,
            isUfo: this.query.typeObject,
            areaIds: this.query.area ? [this.query.area.id] : null,
            // startTime: "2021-03-24 07:35:00",
            // endTime: "2021-03-24 23:00:00",

            limit: 500000,
          });
        }
        if (listAIS.status == 200) {
          // if (!this.urlParamQuery.objId) {
          //   this.$notify({
          //     title:
          //       "Tìm thấy " +
          //       (listAIS.data ? listAIS.data.length : 0) +
          //       " đối tượng.",
          //     type: "success",
          //   });
          // }
          if (listAIS.data == null || listAIS.data.length == 0) {
            this.listAISShow = [];
            this.query.total = 0;
            this.$notify({
              title: "Thông báo",
              message: "Không tìm thấy đối tượng nào.",
              // type: "error",
            });
            return;
          }
          if (this.query.vesselGroup != "" && this.query.vesselGroup != null) {
            this.isShowListObjectButton = true;
          }
          // if (!this.$route.query) {
          this.openDialogListVessel();
          // }

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
              label: "đối tượng",
            });
          }

          if (callback) {
            callback();
          }
        } else {
          alert("Get list vessel error.");
          return;
        }
        if (!drawLineStringGroup) {
          // vector = new VectorLayer({
          //   source: new Cluster({
          //     distance: 40,
          //     source: new VectorSource({
          //       // format: new GeoJSON(),
          //       features: this.lstVesselFeature,
          //       // url:
          //       //   "https://openlayers.org/en/latest/examples/data/kml/2012_Earthquakes_Mag5.kml",
          //       // format: new KML({
          //       //   extractStyles: false,
          //       // }),
          //     }),
          //   }),
          //   style: styleFunctionClutter,
          // });
          // map.addLayer(vector);
        } else {
          this.drawLineStringGroup(this.listAIS);
          this.closeDialogSearchListVessel();
          this.isPlayback = true;
        }
      } catch (error) {
        console.log("get list ais fail: ", error);
        this.$notify({
          title: "Lỗi",
          message: "Lấy danh sách đối tượng thất bại.",
          position: "top right",
          type: "error",
        });
      }
      // this.isShowDialog = false;
    },

    async openListVesselVoyageDialog() {
      this.isListVesselVoyageDialog = true;
      if (
        this.voyage.listVesselToVoyage &&
        this.voyage.listVesselToVoyage.length > 0
      ) {
        for (var i = 0; i < this.voyage.listVesselToVoyage.length; i++) {
          var itemVessel = this.voyage.listVesselToVoyage[i];
          var objectDetailDialog = {};
          const overallApi = new OverallApi();
          var res;
          if (itemVessel.isUfo == 1) {
            if (itemVessel && typeof itemVessel.objId === "number") {
              return;
            }
            res = await overallApi.postApi(
              "/contact/Object/detail/getDetailObjectInfo",
              {
                uuid: itemVessel.objId, // data.objId,
              }
            );
            if (res.status == 200) {
              objectDetailDialog =
                res.data != null ? res.data.objectUnInfoDto : {};
            }
          } else {
            res = await overallApi.postApi(
              "/ais/vessel/detail/" + itemVessel.objId
            );
            if (res.status == 200) {
              objectDetailDialog = res.data != null ? res.data : {};
            }
          }

          // console.log("getDetail : ", objectDetailDialog);
          this.voyage.listVesselToVoyage[i] = {
            ...this.voyage.listVesselToVoyage[i],
            ...objectDetailDialog,
          };
        }
      }

      this.rerenderShowListVesselDetail();
      console.log(
        "getDetail listVesselToVoyage: ",
        this.voyage.listVesselToVoyage
      );
    },

    closeListVesselVoyageDialog() {
      this.isListVesselVoyageDialog = false;
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

    clearAllFeature() {
      try {
        this.intervalPlayback && clearInterval(this.intervalPlayback);
        const layers = [...map.getLayers().getArray()];
        console.log("clear all 1", layers);
        layers.splice(0, 1);
        console.log("clear all 2", layers);
        layers.forEach((layer) => map.removeLayer(layer));

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

    clearAllFeaturePlayback() {
      try {
        this.intervalPlayback && clearInterval(this.intervalPlayback);
        const layers = [...map.getLayers().getArray()];
        console.log("clear all 1", layers);
        layers.splice(0, 1);
        console.log("clear all 2", layers);
        layers.forEach((layer) => {
          debugger;
          if (layer.values_.isPlayback) {
            map.removeLayer(layer);
          }
        });
      } catch (error) {
        console.log("error clear all feature");
      }
    },

    clearAllFeatureDrawMeasure() {
      try {
        const layers = [...map.getLayers().getArray()];

        layers.splice(0, 1);

        layers.forEach((layer) => {
          if (layer.values_.isMeasure) {
            console.log("clear all 1", layer);
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
      // console.log("re center");
      var location = hanoi;
      if (longitude && latitude) {
        location = [longitude, latitude];
      }
      var view = new View({
        projection: "EPSG:4326",
        center: location,
        zoom: zoom ? zoom : map.getView().getZoom() + 5,
      });
      view.centerOn(location, map.getSize(), [
        map.getSize()[0] / 2 + 110,
        map.getSize()[1] / 2 + 160,
      ]);
      map.setView(view);
    },

    handleDrawVung() {
      if (this.vung.name == "" || this.vung.name == null) {
        this.$notify({
          title: "Lỗi",
          message: "Tên vùng là bắt buộc",
          position: "top right",
          type: "error",
          position: "top-center",
        });
        return;
      }

      this.isShowVung = false;
      var value = this.vung.vungType;
      console.log("type ", value);
      if (value && value !== "None") {
        this.vung.isDrawing = true;
        currentDraw = new Draw({
          source: currentDrawSource,
          type: value,
          freehand: true,
        });
        currentDraw.on("drawend", (event) => {
          this.featureID = this.featureID + 1;
          console.log("drawend", this);
          event.feature.setProperties({
            id: this.featureID,
            name: "" + this.vung.name + "",
            dataVung: Object.assign({}, this.vung),
            type: "area",
          });
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
        // if (!this.areaQuery.startTime || !this.areaQuery.endTime) {
        //   this.$notify({
        //     title: "Lỗi",
        //     message: "StartTime và EndTime là bắt buộc",
        //     position: "top right",
        //     type: "error",
        //   });
        //   return;
        // }
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
          position: "top right",
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
        this.selectedFeature = properties.dataVung;
        this.$notify({
          title: "Chọn vùng",
          message: "Vùng " + properties.name + " đã được chọn.",
          position: "top right",
          type: "info",
        });
        console.log("select feature,", properties);
      });
      this.singleClick.getFeatures().on("remove", (event) => {
        this.selectedFeatureID = null;
        this.selectedFeature = {};
        console.log("deselected", event);
      });
    },
    removeSelect() {
      map.removeInteraction(this.singleClick);
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
  },
};

// First some change in this function.
</script>

<style lang="scss">
@import "~ol/ol.css";
#voyage {
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
    height: calc(100% - 60px) !important;
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
    left: -500px;
    // top: 200px;
    // left: 200px;
    min-width: 467px !important;
  }
  .label-form {
    min-width: 70px;
    width: auto;
    float: left;
    margin-right: 6px;
  }
  .el-dialog__wrapper {
    overflow: unset;
  }
  .ol-popup label {
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
  .el-button-on-map .el-icon-map-location {
    margin-left: 0px !important;
  }
  #dl-list-vessel-voyage-position {
    left: 69%;
  }
  #dl-list-vessel-voyage-position {
    // margin-top: 10vh;
    // z-index: auto;
    // max-width: 800px;
    // min-width: 350px;
    // width: auto;
    // height: max-content;
    width: 360px;
  }
  #dl-list-vessel-voyage-position2 {
    width: 700px;
    left: calc(35% - 350px);
  }
  #dl-list-vessel-voyage-position input {
    width: 100%;
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
  .ol-popup-closer {
    text-decoration: none;
    position: absolute;
    top: 2px;
    right: 8px;
  }
  .ol-popup-closer:after {
    content: "✖";
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
  .tab-content {
    margin-top: 2 !important;
  }
  .table-detail {
    border: 0.25px solid #cecece;
    margin-top: -1px;
    padding-top: 2px;
    padding-bottom: 2px;
  }
  .container-history {
  }
  .text-des {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
  }
  #dl-VoyageDetailDialog {
    left: 512px;
    top: 10px;
    width: 320px;
  }

  #dl-ListVoyageDialog {
    left: 60px;
    top: 10px;
    width: 450px;
  }
  .content-voyage-detail {
    width: 100%;
    height: 600px;
    overflow: auto;
    overflow-x: hidden;
  }
  .content-voyage-detail .vessel-wrapper-detail {
    cursor: pointer;
    position: relative;
    border: 0.5px solid #cecece;
    height: 100px;
    padding: 6px;
  }
  #dl-ListVoyageDialog button {
    text-align: left;
  }
  .vessel-wrapper-detail {
    padding-bottom: 6px;
    border: 1px solid #cecece;
    border-radius: 0px;
  }
  .vessel-wrapper-detail button{
    border-radius: unset;
  }
  .point-vessel-voyage {
    position: absolute;
    left: 99px;
    top: 10px;
    width: 16px;
    height: 16px;
    border-radius: 50%;
    border: 2px solid rgb(58, 57, 57);
  }
  .line-vessel-voyage {
    z-index: 10;
    position: absolute;
    left: 106px;
    top: 26px;
    width: 2px;
    height: calc(100% - 14px);
    border-radius: 10%;
    background-color: rgb(14, 12, 12);
  }
  .lb-des-point-vessel {
    width: 75px;
    word-wrap: break-word;
    text-align: center;
  }
  .content-list-vessel-voyage {
    height: 600px;
    overflow-y: auto;
    overflow-x: hidden;
  }
  #dl-list-vessel-voyage-position .el-dialog__body {
    height: 600px;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .el-dialog__body {
    padding: 8px;
  }
  .el-dialog__footer {
    position: sticky;
    bottom: 0;
  }
  #popup-content {
    padding-left: 10px;
    padding-right: 10px;
  }
  .note-list {
    width: 100%;
    height: 250px;
    overflow: auto;
    border: 0.5px solid #cecece;
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
  .con-playback{
    width: 100%;
    height: 80px;
  }
  .button-playback {
    width: 80px;
    float: left;
    margin: 6px;
  }
  .nav-link.active {
    border-top: 2px solid #11a0d9 !important;
  }
  .bar-playback-parrent {
    width: calc(100% - 200px);
    float: left;
  }
  .bar-playback {
    position: relative;
    width: 100%;
    float: left;
    margin: 6px;
    background: #121212;
  }

  .track {
    height: 0.5rem;
    background: #0060f6;
    border-radius: 0.25rem;
    margin-top: 1.25rem;
    margin-bottom: 1.25rem;
  }
  #search-by-group-dialog .el-dialog__body {
  }

  #search-by-group-dialog {
    width: 480px;
    left: calc(70% - 180px);
    top: calc(60% - 60px);
  }

  #playback-dialog .el-dialog__body {
    background-color: #121212;
    height: 65px;
  }
  #playback-dialog .el-dialog__footer {
    display: none !important;
  }
  #playback-dialog {
    width: 630px;
    left: calc(70% - 180px);
    top: calc(80% - 60px);
  }

  .knob {
    position: absolute;
    width: 1rem;
    height: 1rem;
    border-radius: 50%;
    background: white;
    transform: translate(-25%, -25%);
  }
  .knob:hover {
    cursor: pointer;
  }

  .clear {
    clear: both;
  }
  .collapsible {
    background-color: #777;
    color: white;
    cursor: pointer;
    padding: 18px;
    width: 100%;
    border: none;
    text-align: left;
    outline: none;
    font-size: 15px;
  }

  .active,
  .collapsible:hover {
  }
  .note-list {
    width: 100%;
    height: 230px;
    overflow: auto;
    border: 1px solid #cecece;
  }
  .row {
    margin-right: 0px;
    margin-left: 0px;
  }
  #dialog-addToGroup {
    width: 450px;
    left: calc(50% - 250px);
  }
  #popup {
    padding: 0;
    border-radius: 3px;
  }
  #popup #popup-content {
    padding: 6px;
    height: 387px;
  }
  .popup-header {
    height: 40px;
    background-color: #0d518c;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
  }
  .ol-popup-closer {
    color: #fff;
  }
  .ol-popup-closer {
    margin-top: 5px;
  }
  .info-history {
    width: 100%;
    height: 200px;
    text-align: center;
  }
  #popup {
    padding: 0;
    border-radius: 3px;
  }
  .popup-header {
    height: 40px;
    background-color: #0c518c;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
  }
  .content {
    padding: 0 18px;
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.2s ease-out;
    background-color: #f1f1f1;
  }
  .button-collapse {
    width: 100%;
    margin-bottom: 4px;
    border-radius: none;
  }
  
  .date-overlay {
    position: absolute;
    bottom: 27px;
    right: 148px;
    z-index: 1000;
    width: 700px;
  }
  .el-button-on-map button {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }

  .el-button-on-map i {
    margin-left: -6px !important;
  }
  #dl-list-vessel-voyage-position2 .el-dialog__body {
    height: 473px;
    overflow: auto;
    overflow-x: hidden;
  }
  textarea:focus-visible {
    outline: none;
  }
}
.v-modal {
    position: unset !important;
    z-index: -2 !important;
  }
</style>

