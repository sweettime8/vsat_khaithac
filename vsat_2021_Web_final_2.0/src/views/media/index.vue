<template>
  <div class="app-container">
    <h2 class="no-margin-top">Danh sách Media</h2>
    <div class="filter-container">
      <template>
        
        <div class="block">
          <el-button class="search" v-loading="loading" @click="downloadFiles()" type="primary" size="small">Download</el-button>
        </div>
        <div class="block">
          <el-input v-model="query.sourceIps" placeholder="Ip nguồn" size="small"></el-input>
        </div>
        <div class="block">
          <el-input v-model="query.destIps" placeholder="Ip đích" size="small"></el-input>
        </div>
        <div class="block">
          <el-select id="sltMediaType" multiple collapse-tags v-model="query.mediaType" clearable placeholder="Loại media" size="small">
            <el-option v-for="item in mediaTypeLst" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </div>
        <div class="block">
          <el-select id="sltFileType" multiple collapse-tags v-model="query.fileType" clearable placeholder="Định dạng" size="small">
            <el-option v-for="item in fileTypeLst" :key="item.name" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </div>
        <div class="block">
          <el-select id="sltDataSource" multiple collapse-tags v-model="query.dataSource" clearable placeholder="Nguồn thu" size="small">
            <el-option v-for="item in dataSourceLst" :key="item.id" :label="item.sourceName" :value="item.id"></el-option>
          </el-select>
        </div>
        <div class="block">
          <el-date-picker v-model="query.startTime" id="startTimeInput" size="small" format="dd/MM/yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false" type="datetime" style="width: 160px;" placeholder="Từ ngày"></el-date-picker>
        </div>
        <div class="block">
          <el-date-picker v-model="query.endTime" id="endTimeInput" size="small" format="dd/MM/yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false" type="datetime" style="width: 150px; margin-left: 15px;" placeholder="Đến ngày"></el-date-picker>
        </div>
        <div class="block">
          <el-button class="search" @click="handleFilter()" id="btnSearch" type="primary" size="small">Tìm kiếm</el-button>
        </div>
        
      </template>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="loading" border fit highlight-current-row 
          @selection-change="handleSelectionChange()" style="width: 100%">
      
      <el-table-column align="center" type="selection" width="30">
        <template slot-scope="scope">
          <span><input type="checkbox" v-model="checkedLst" :value="scope.row" /></span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="STT" width="65">
        <template slot-scope="scope">
          <span>{{
            (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
          }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Nguồn thu" width="190">
        <template slot-scope="scope">
          <span>{{ scope.row.dataSourceName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Loại" width="105">
        <template slot-scope="scope">
          <span>{{ scope.row.mediaTypeName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Ip nguồn" width="120" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.sourceIp }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Ip đích" width="120" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.destIp }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Chiều dữ liệu" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.direction == 1">Chiều đi</span>
          <span v-else-if="scope.row.direction == 2">Chiều về</span>
          <span v-else>Không xác định</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Thời gian" width="140">
        <template slot-scope="scope">
          <span>{{ scope.row.eventTime | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Dung lượng" width="125">
        <template slot-scope="scope">
          <span>{{ scope.row.fileSize }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Trạng thái" width="100" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0"><el-tag effect="light">Chưa xem</el-tag></span>
          <span v-else><el-tag type="success" effect="light" >Đã xem</el-tag></span>
        </template>
      </el-table-column>

      <el-table-column label="Tác vụ" align="center">
        <template slot-scope="scope">

          <el-button type="primary" size="small" @click="viewDetailMedia(scope.row)">Chi tiết</el-button>

          <!-- form xem chi tiết -->
          <el-dialog top="10px" title="Thông tin" :visible.sync="dialogMediaDetailVisible" width="80%"
           :close-on-click-modal="false" @close="closeDialog()">
            
              <el-row :gutter="5">
                <el-col :span="12">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD; max-height: 260px; overflow-y:auto;">

                    <!-- foreach comment media -->
                    <el-row v-for="item in comments" :key="item.id" style="border: 1px solid #FFC9CD;">
                        <el-col :span="5" style="font-size: 11px; background: #A3A3A3; color: white;">{{ item.createUser }}<br />{{ item.createdTime | formatDate }}</el-col>
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
                <el-col :span="12">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">

                    <!-- Nếu media là Web/html thì gen html ra trước, còn các loại media khác thì gen html = JS -->
                    <div v-if="mediaDetail && mediaDetail.mediaTypeId === 3">
                      <div class="row" style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878; margin-left: 0; margin-right: 0; height: 255px">
                        <div style="height: 15px"></div>
                        <div class="form-row" style="width: 100%">
                          <div class="col-sm-9">
                            <span style="color: red; font-weight: bold">&emsp;{{mediaDetail.filePath}}</span>
                          </div>
                          <div v-if="mediaDetail.filePath.endsWith('.html')" class="col-sm-1 text-right">
                            <a href="javascript:void(0)" @click="viewHtmlWeb(mediaDetail.filePath)" style="color: orange; font-weight: bold">Xem</a>
                          </div>
                          <div class="col-sm-2 text-left">
                            <a v-bind:href="mediaDetail.filePath" style="color: blue; font-weight: bold" target="_blank" v-bind:download="mediaDetail.filePath">Tải xuống</a>
                          </div>
                        </div>
                      </div>
                    </div>
                    <!-- Hiển thị media: audio, video, .... -->
                    <div ref="mediaModelVideoAudio"></div>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="5">
                <el-col :span="12">
                  <div class="grid-content bg-purple-light" style="border: 1px solid #FFC9CD;">
                    <el-row style="margin: 2px;">
                      <el-col :span="18" class="aliasLeft">
                          <el-input v-model="commentValue" id="addCommentInput" placeholder="Nhập ghi chú ..." size="small"></el-input>
                      </el-col>
                      <el-col :span="6" class="textRight">
                        <el-button v-loading="loading" class="search" @click="addCommentMedia()" type="primary" size="small">Đồng ý</el-button>
                      </el-col>
                    </el-row>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="grid-content bg-purple-light" style="border: 1px solid #FFC9CD;">
                    <el-row style="margin: 2px;">
                      <!-- Cắt và tải về -->
                    </el-row>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="5">
                <el-col :span="6">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="6" class="aliasLeft">ID nguồn</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.srcId }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Loại</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.srcIsHq === 1 ? 'Bờ' : 'Trên biển' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Tên</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.srcName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">SĐT</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.sourcePhone }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">IP</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.sourceIp }}</el-col>
                    </el-row>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="6" class="aliasLeft">ID đích</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.destId }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Loại</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.destIsHq === 1 ? 'Bờ' : 'Trên biển' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Tên</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.destName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">SĐT</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.destPhone }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">IP</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetail.destIp }}</el-col>
                    </el-row>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Loại media</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetail.mediaTypeName }}</el-col>
                      <el-col :span="4" class="aliasLeft">Trạng thái</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetail.status === 1 ? 'Đã xem' : 'Chưa xem' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Thời gian</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetail.eventTime | formatDate }}</el-col>
                      <el-col :span="4" class="aliasLeft">Nguồn thu</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetail.dataSourceName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Dung lượng</el-col>
                      <el-col :span="20" class="textRight">{{ mediaDetail.fileSize }}</el-col>
                    </el-row>
                  </div>
                </el-col>
              </el-row>

              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogMediaDetailVisible = false">Đóng</el-button>
              </div> 
          </el-dialog>

          <el-dialog top="10px" title="Html page" :visible.sync="dialogMediaDetailHtmlWebVisible" width="90%" height="100%" :close-on-click-modal="false">
              <el-row :gutter="5">
                <el-col :span="24">
                  <div v-if="mediaDetail && mediaDetail.mediaTypeId === 3 && mediaDetail.filePath.endsWith('.html')" class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row style="border: 1px solid #FFC9CD;">
                      <el-col :span="24">
                        <iframe id="mediaListModelCommonViewWebHtmlIframe" :src="mediaDetail.filePath" width="100%" height="100%" frameBorder="0"></iframe>
                      </el-col>
                    </el-row>
                  </div>
                </el-col>
              </el-row>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogMediaDetailHtmlWebVisible = false">Đóng</el-button>
              </div> 
          </el-dialog>

        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 10" :total="total" :page.sync="query.currentPage" :limit.sync="query.rowsPerPage" @pagination="getList" />

  </div>
</template>

<script>
import Pagination from "@/components/Pagination";
import Resource from "@/api/resource";
import axios from 'axios';

const dataSourceResource = new Resource("manager/source/get-all");
const mediaTypeResource = new Resource("manager/media-type/get-all");
const mediaResource = new Resource("media/list/search");
const updateMediaReadStatusResource = new Resource("media/list/updateStatusMediaId");
const addCommentResource = new Resource("media/list/addCommentCommon");
const getListCommentResource = new Resource("media/list-comments");
const mediaFetchM3U8FileResource = new Resource("media/fetch-m3u8-file");

let today = new Date();
let day = checkZero(today.getDate() + '');
let month = checkZero((today.getMonth() + 1) + '');

export default {
  components: {
    Pagination,
  },
  directives: {},
  data() {
    return {
      dialogMediaDetailVisible: false,
      dialogMediaDetailHtmlWebVisible: false,
      list: null,
      total: 0,
      loading: true,
      query: {
        startTime: today.getFullYear() + '-' +  month + '-' +  day + ' 00:00:00',
        endTime: getCurrentDateToStr(today),
        currentPage: 1,
        rowsPerPage: 20,
        sourceIps: "",
        destIps: "",
        mediaType: [],
        fileType: [],
        dataSource: []
      },
      dataSourceLst: [],
      mediaTypeLst: [],
      fileTypeLst: [],
      mediaDetail: {},
      comments: [],
      commentValue: '',
      checkedLst: [],
      multipleSelection: [],
      allSelected: false
    };
  },
  created() {
    this.fileTypeLst = [{name: '.mp3'},{name: '.wav'},{name: '.mp4'},{name: '.ts'},{name: '.html'}
    ,{name: '.jpg'},{name: '.doc'},{name: '.docx'},{name: '.pdf'},{name: '.xls'},{name: '.xlsx'},{name: '.rar'}
    ,{name: '.ppt'},{name: '.pptx'},{name: '.eml'},{name: '.json'},{name: '.txt'},{name: '.git'},{name: '.g711mu'}
    ,{name: '.g711a'},{name: '.g722'},{name: '.g7221'},{name: '.g723'},{name: '.g7231'},{name: '.g726'},{name: '.g727'}
    ,{name: '.g728'},{name: '.g729'},{name: '._g719'},{name: '.ESP'},{name: '.UDP'},{name: '.TCP'},{name: '.AH'}];
    this.getList();
    this.getAllDataSource();
    this.getAllMediaType();
  },
  methods: {
    async getAllDataSource() {
      const { data } = await dataSourceResource.list();
      if(data)
        this.dataSourceLst = data;
    },
    async getAllMediaType() {
      const { data } = await mediaTypeResource.list();
      if(data)
        this.mediaTypeLst = data;
    },
    getList() {
      if( this.query.startTime >= this.query.endTime ) {
        alert('Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!');
        return;
      }

      this.loading = true;
      this.checkedLst = [];
      console.log('==> query: ' + JSON.stringify(this.query));
      mediaResource.listByPost(this.query).then((mediaDataLst) => {
        // console.log('<== mediaDataLst: [' + JSON.stringify(mediaDataLst) + ']');
        if( mediaDataLst.data && mediaDataLst.data.length > 0 ) {
          this.list = mediaDataLst.data;
          this.total = mediaDataLst.total;

          this.list.forEach(item => {
            item.fileSize = commonFormatBytesToSize(item.fileSize);
          });
        }else {
          console.info('mediaDataLst is empty!');
          this.list = [];
          this.total = 0;
        }
        this.loading = false;
      }).catch((err) => {
        console.error(err);
        this.loading = false;
      });
    },
    mediaListBuildVideoAudioByMediaId(filePath, mediaTypeId) {
      let html = '';
      if (mediaTypeId === 1) // audio
        html = '<video id="mediaPlayerId" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
                '    <source src="' + filePath + '" type="audio/wav">' +
                '</video>';
      else if ( mediaTypeId === 2 ) { // video
          if ( filePath.endsWith('.ts') ) // video stream
              html = '<video id="mediaPlayerId" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
                      '</video>';
          else // mp4, etc,....
              html = '<video id="mediaPlayerId" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
                      '    <source src="' + filePath + '" type="video/mp4">' +
                      '</video>';
      } else if (mediaTypeId === 3) { // Web
          // Không gen ra html vì đã gen html sẵn rồi ( <div v-if="mediaDetail.mediaTypeId === 3"> ), case này code ra nhìn cho rõ ràng chứ ko có case này xảy ra được, do đã check mediaTypeId <> 3 từ trước khi vào hàm này.
      } else { // Email || transfer file
          html = '<div class="row" style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878; margin-left: 0; margin-right: 0; height: 255px">' +
                  '     <div style="height: 15px"></div>' +
                  '     <div class="form-row" style="width: 100%">' +
                  '         <div class="col-sm-9">' +
                  '             <span style="color: red; font-weight: bold">&emsp;' + filePath + '</span>' +
                  '         </div>';
          if ( mediaTypeId !== 8 && filePath.endsWith('.html') ) {
              html += '         <div class="col-sm-1 text-right">' +
                      '             <a href="' + filePath + '" style="color: orange; font-weight: bold" target="_blank">Xem</a>' +
                      '         </div>';
          }
          html += '				<div class="col-sm-2 text-left">' +
                  '             <a href="' + filePath + '" style="color: blue; font-weight: bold" target="_blank">Tải xuống</a>' +
                  '         </div>' +
                  '     </div> ' +
                  ' </div>';
      }
      return html;
    },
    viewHtmlWeb(filePath) {
      console.warn('filePath: ' + filePath);
      this.dialogMediaDetailHtmlWebVisible = true;
    },
    viewDetailMedia(media) {
        this.dialogMediaDetailVisible = true;

        this.mediaDetail = media;
        console.log('<== mediaDetail: ' + JSON.stringify(this.mediaDetail))

        let payLoadGetComment = {
          commentTypeId: 3,
          refId: this.mediaDetail && this.mediaDetail.uuid ? this.mediaDetail.uuid : 'EMPTY'
        };
        this.comments = [];
        getListCommentResource.listByPost(payLoadGetComment).then((res) => {
          console.log('<== getListCommentResource.res: [' + JSON.stringify(res) + ']');
          if( res && res.data && res.data.length > 0 )
            this.comments = res.data;
        }).catch((err) => {
          console.error(err);
        });

        if( this.mediaDetail.filePath ) {

          //Đánh dấu đã xem
          let payLoad = {
            uuidKeys: [this.mediaDetail.uuid]
          };
          updateMediaReadStatusResource.listByPost(payLoad).then((res) => {
            console.log('<== updateMediaReadStatusResource.res: [' + JSON.stringify(res) + ']');
            if( res && res.data )
              media.status = 1;
          }).catch((err) => {
            console.error(err);
          });

          if( this.mediaDetail.mediaTypeId === 3 ) {
            // Xóa phần hiển thị cũ như Audio, video, .... nếu trước đó đã từng gen ra content.
            const mediaElement = this.$refs.mediaModelVideoAudio;
            if( mediaElement )
              mediaElement.innerHTML = '';

            //Return, ko xử lý gen html từ JS nữa.
            return;
          }

          let htmlVideoAudio = this.mediaListBuildVideoAudioByMediaId(this.mediaDetail.filePath, this.mediaDetail.mediaTypeId);
          this.$nextTick(() => {

            const mediaElement = this.$refs.mediaModelVideoAudio;
            if( mediaElement )
              mediaElement.innerHTML = htmlVideoAudio;

            if( this.mediaDetail.filePath.endsWith('.ts') && this.mediaDetail.filePathLocal ) {
              console.log('filePathLocal: ' + this.mediaDetail.filePathLocal)
              let payLoad = {
                filePathLocal: this.mediaDetail.filePathLocal
              };

              mediaFetchM3U8FileResource.listByPost(payLoad).then((res) => {
                console.warn('<== fetch-m3u8-file return: ' + JSON.stringify(res));
                if( res.data ) {
                  let m3u8Resource = res.data;
                  var videoData = document.getElementById('mediaPlayerId');
                  if (Hls.isSupported()) {
                    var hls = new Hls();
                    hls.loadSource(m3u8Resource);
                    hls.attachMedia(videoData);
                    hls.on(Hls.Events.MANIFEST_PARSED, function () {
                        videoData[0].play();
                    });
                  }
                }
              }).catch((e) => {
                console.error(e);
              });

              /*axios.post(`http://192.168.61.106:8415/v1.0/media/fetch-m3u8-file`, payLoad)
              .then(res => {
                console.warn('<== fetch-m3u8-file return: ' + JSON.stringify(res));
                if( res.data && res.data.data ) {
                  let m3u8Resource = res.data.data;
                  var videoData = document.getElementById('mediaPlayerId');
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
              .catch(e => {
                console.error(e);
              });*/

            }
          });
        }
    },
    handleFilter() {
      this.query.currentPage = 1;
      this.getList();
    },
    addCommentMedia() {

      if( !this.commentValue || this.commentValue.trim === '' ) {
        alert('Chưa nhập nội dung comment!');
        return;
      }

      this.loading = true;
      let item = {
        createdTime: new Date(),
        createUser: this.$store.getters.userName,
        content: this.commentValue,
        commentTypeId: 3,
        refId: this.mediaDetail && this.mediaDetail.uuid ? this.mediaDetail.uuid : 'EMPTY'
      };
      addCommentResource.listByPost(item).then((res) => {
        console.log('<== addCommentResource.res: [' + JSON.stringify(res) + ']');
        if( res && res.data ) {
          this.commentValue = '';
          this.comments.push(item);
        }
        this.loading = false;
      }).catch((err) => {
        console.error(err);
        this.loading = false;
      });
    },
    selectAll() {
        if( this.checkedLst.length > 0 )
          this.checkedLst = [];
        if ( !this.allSelected ) {
            for (media in this.list) {
                this.checkedLst.push(this.media);
            }
        }
    },
    downloadFiles() {
      this.loading = !this.loading;
      // console.log(JSON.stringify(this.checkedLst));
      if( this.checkedLst.length > 0 ) {
        axios.post(process.env.VUE_APP_FILE_API + 'download-media-files'
                , { mediaFiles: this.checkedLst }
                , { responseType: 'blob' })
        .then(res => {
          if( res.data ) {
            const blob = new Blob([res.data], { type: res.data.type });
            const link = document.createElement('a');
            link.href = URL.createObjectURL(blob);
            console.warn(res);
            link.setAttribute('download', 'vsat-media-files.zip');
            link.click();
            URL.revokeObjectURL(link.href);
          }
          this.loading = !this.loading;
        })
        .catch(e => {
          console.error(e);
          this.loading = !this.loading;
        });
      }else {
        alert('Chưa chọn file cần tải!');
        this.loading = !this.loading;
      }
    },
    toggleSelection(rows) {
      if( rows ) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      }else
        this.$refs.multipleTable.clearSelection();
    },
    // handleSelectionChange(val) {
    //   this.multipleSelection = val;
    // }
    handleSelectionChange() {
      if( this.list.length === 0 )
        return;
      if( this.checkedLst.length > 0 )
        this.checkedLst = [];
      if ( !this.allSelected ) {
        this.checkedLst = Array.from(this.list);
        this.allSelected = true;
      }else
        this.allSelected = false;
    },
    closeDialog () {
      stopPlayMedia('mediaPlayerId');
    }
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
  width: 150px;
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
    background: #F2F2F2;
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
  #sltFileType {
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
