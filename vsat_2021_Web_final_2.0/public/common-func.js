
function stopPlayMedia(mediaId) {
  let mediaObj = $("#" + mediaId);
  if ( mediaObj && mediaObj[0] ) {
    mediaObj[0].pause();
    mediaObj[0].currentTime = 0;
  }
}

function commonFormatBytesToSize(bytes, decimals) {
  if( bytes && bytes > 0 ) {
      var k = 1024, dm = decimals <= 0 ? 0 : decimals || 2,
              sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
              i = Math.floor(Math.log(bytes) / Math.log(k));
      rs = parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
      // console.warn('size:: ' + rs);
      return rs;
  }else if ( isNaN(bytes) )
    return bytes;
  else
    return "N/A";
}

function getCurrentDateToStr(today) {
  let day = checkZero(today.getDate() + '');
  let month = checkZero((today.getMonth() + 1) + '');
  let year = today.getFullYear() + '';
  let hour = checkZero(today.getHours() + '');
  let minutes = checkZero(today.getMinutes() + '');
  let seconds = checkZero(today.getSeconds() + '');
  return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
}

function checkZero(data) {
  return data.length === 1 ? '0' + data : data;
}

function formatDateTimeYYYYMMDD(value) {
  if (!value) return ''
  var year = value.substring(0, 4);
  var month = value.substring(4, 6);
  var day = value.substring(6, 8);
  var dateTimeReturn = day + '/' + month + '/' + year;
  return dateTimeReturn;
}

function commonGetDateFromYearAndMonth(month, year, fromTo){
  let dayOfMonth = 0;
	if(fromTo == "from"){
		dayOfMonth = 1;
    month = parseInt(month, 10)-1;
	} else {
		dayOfMonth = 0;
		month = parseInt(month, 10);
	}
	let date_by_year = new Date(year, month, dayOfMonth);
	let date = moment(date_by_year, 'DD/MM/YYYY HH:mm:ss');
	return date.format('YYYY-MM-DD HH:mm:ss');
}

function commonGetDateFromYearAndWeek(week, year, fromTo){
	let week_number = parseInt(week, 10);
	let year_number = parseInt(year, 10);
	if(fromTo == "from"){
		return commonGetDateFromYearAndWeekStart(week_number, year_number);
	} else {
		return commonGetDateFromYearAndWeekEnd(week_number, year_number);
	}
}

function commonGetDateFromYearAndWeekStart(w, y){
   let simple = new Date(y, 0, 1 + (w - 1) * 7);
   let dow = simple.getDay();
   let ISOweekStart = simple;
   if (dow <= 4) {
       ISOweekStart.setDate(simple.getDate() - simple.getDay() + 1);
   } else {
       ISOweekStart.setDate(simple.getDate() + 8 - simple.getDay())
   };
   return moment(ISOweekStart).format('YYYY-MM-DD HH:mm:ss');
}

function commonGetDateFromYearAndWeekEnd(w, y){
   let simple = new Date(y, 0, 1 + (w - 1) * 7);
   let dow = simple.getDay();
   let ISOweekStart = simple;
   if (dow <= 4) {
       ISOweekStart.setDate(simple.getDate() - simple.getDay() + 1);
   } else {
       ISOweekStart.setDate(simple.getDate() + 8 - simple.getDay())
   };
   var lastday = ISOweekStart.getDate() - (ISOweekStart.getDay() - 1) + 6;
   ISOweekStart.setDate(lastday);
 return moment(ISOweekStart).format('YYYY-MM-DD HH:mm:ss');
}

function commomColor(){
	return ['#666EE8', '#28D094', '#FF4961', '#1E9FF2', '#FF9149'
        ,'#9C27B0', '#00A5A8', '#FF7D4D','#222222', '#CC0000'
        , '#660000', '#FFFFFF','#CCFFFF', '#33FFFF','#FFCCFF'
        ,'#99CCFF', '#33CCFF','#00CC33','#FF9933','#999933'
        , '#009933', '#FF6633','#996633','#006633','#FF3333'
        ,'#333333', '#003366', '#FF0066', '#990066', '#000066'
        , '#00BB00', '#0000EE','#FF99FF', '#3399FF', '#FF66FF'
        , '#0066FF', '#FF0000','#00FF00','#FFFF66','#33FF33'
        ,'#001100','#009966','#006666','#FF0099', '#006600'
        ,"#04B431", "#0B6121","#8A0868","#0A0A2A","#F3E2A9"
        ,"#58FAAC","#04B4AE","#F781D8","#D358F7"];
}


function formatCapacity(bytes){
  var decimals = 2;
  if ( bytes && bytes > 0 ) {
    let k = 1024, dm = (!decimals || decimals <= 0) ? 0 : decimals || 2,
            sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }
  return '0 Bytes';
    
}

function commonMonthKeyToText(number_month){
	return parseInt(number_month, 10) + 1;
}

function commonRemoveSlash(string){
	return string.replace(/\//g, '');
}
