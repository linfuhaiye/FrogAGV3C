const electron = require("electron");

const { app, BrowserWindow } = electron;

const areaTypeString = process.env.AREA_TYPE;

const isDevelopment = false;
if (isDevelopment) {
  require("electron-reload")(__dirname, {
    ignored: /node_modules|[\/\\]\./
  });
}

let mainWnd = null;

function createMainWnd() {
  mainWnd = new BrowserWindow({
    width: 4096,
    height: 2160,
    minWidth: 1024,
    minHeight: 768,
    closable: true, // 窗口是否可以关闭
    minimizable: true, // 窗口是否可以最小化
    fullscreenable: false, // 是否允许进入全屏模式
    alwaysOnTop: false, // 是否始终保持在其他窗口之上
    skipTaskbar: false, // 是否在任务栏中隐藏窗口
    disableAutoHideCursor: true, // 是否在键入时隐藏光标
    frame: true, // 是否显示框
    resizable: false, // 是否可调整大小
    movable: false, // 是否可移动
    // title: "", //
    icon: "public/img/app-icon.png"
  });

  if (isDevelopment) {
    mainWnd.webContents.openDevTools();
  }


  // 拆包间
  mainWnd.loadURL(`file://${__dirname}/dist/index.html#/demolition/task`);
  // // 消毒间
  // mainWnd.loadURL(`file://${__dirname}/dist/index.html#/disinfection/task`);
  // // 包材仓
  // mainWnd.loadURL(`file://${__dirname}/dist/index.html#/materials/pack`);
  // // 生产区
  // mainWnd.loadURL(`file://${__dirname}/dist/index.html#/login`);
  // if (areaTypeString === 'demolition') {
  //   // 拆包间
  //   mainWnd.loadURL(`file://${__dirname}/dist/index.html#/demolition/task`);
  // } else if (areaTypeString === 'disinfection') {
  //   // 消毒间
  //   mainWnd.loadURL(`file://${__dirname}/dist/index.html#/disinfection/task`);
  // } else if (areaTypeString === 'materials') {
  //   // 包材仓
  //   mainWnd.loadURL(`file://${__dirname}/dist/index.html#/materials/pack`);
  // } else {
  //   // 生产区
  //   mainWnd.loadURL(`file://${__dirname}/dist/index.html#/login`);
  // }

  mainWnd.on("closed", () => {
    mainWnd = null;
  });

  // mainWnd.once('ready-to-show', () => {
  //   win.show()
  // })
}

app.on("ready", createMainWnd);

app.on("window-all-closed", () => {
  app.quit();
});
