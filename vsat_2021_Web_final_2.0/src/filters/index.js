import moment from 'moment';

// set function parseTime,formatTime to filter
export { parseTime, formatTime } from '@/utils';

export function pluralize(time, label) {
  if (time === 1) {
    return time + label;
  }
  return time + label + 's';
}

export function timeAgo(time) {
  const between = Date.now() / 1000 - Number(time);
  if (between < 3600) {
    return pluralize(~~(between / 60), ' minute');
  } else if (between < 86400) {
    return pluralize(~~(between / 3600), ' hour');
  } else {
    return pluralize(~~(between / 86400), ' day');
  }
}

/* Number formating*/
export function numberFormatter(num, digits) {
  const si = [
    { value: 1E18, symbol: 'E' },
    { value: 1E15, symbol: 'P' },
    { value: 1E12, symbol: 'T' },
    { value: 1E9, symbol: 'G' },
    { value: 1E6, symbol: 'M' },
    { value: 1E3, symbol: 'k' },
  ];
  for (let i = 0; i < si.length; i++) {
    if (num >= si[i].value) {
      return (num / si[i].value + 0.1).toFixed(digits).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, '$1') + si[i].symbol;
    }
  }
  return num.toString();
}

export function toThousandFilter(num) {
  return (+num || 0).toString().replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','));
}

export function uppercaseFirst(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}

// format date time
export function formatDateString(dateString){
  let date = new Date(dateString)
  let timestamp = date.getTime();
  return formatDate(timestamp)
}
export function formatDate(value) {
  if (!value) return ''
  value = value/1000;  
  value = moment.unix(value).format('HH:mm DD/MM/YYYY');
  return value;
}

export function formatDateValueString(value) {
  if (!value) return ''
  var year = value.substring(0, 4);
  var month = value.substring(4, 6);
  var day = value.substring(6, 8);
  var dateTimeReturn = day + '/' + month + '/' + year;
  return dateTimeReturn;
}



export function timeStampToDate(timeStamp){
  var current = new Date()
  var previous = new Date(timeStamp * 1000);
  var msPerMinute = 60 * 1000;
  var msPerHour = msPerMinute * 60;
  var msPerDay = msPerHour * 24;
  var msPerMonth = msPerDay * 30;
  var msPerYear = msPerDay * 365;
  var elapsed = current - previous;

  if (elapsed < msPerMinute) {
    if(parseInt(Math.round(elapsed/1000)) + 1 <= 59){
      return 'vừa xong';
    }
  }
  else if (elapsed < msPerHour) {
    return Math.round(elapsed/msPerMinute) + ' phút trước';
  }
  else if (elapsed < msPerDay ) {
    return Math.round(elapsed/msPerHour ) + ' giờ trước';   
  }
  else if (elapsed < msPerMonth) {
    return Math.round(elapsed/msPerDay) + ' ngày trước';
  }
  else if (elapsed < msPerYear) {
    return Math.round(elapsed/msPerMonth) + ' tháng trước';
  }
  else {
    return Math.round(elapsed/msPerYear ) + ' năm trước';
  }
}

export function worktimeFilter(time) {
  return time.substring(0, 5);
}

export function formatCapacity(bytes){
  var decimals = 2;
  if ( bytes && bytes > 0 ) {
    let k = 1024, dm = (!decimals || decimals <= 0) ? 0 : decimals || 2,
            sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }
  return '0 Bytes';
    
}