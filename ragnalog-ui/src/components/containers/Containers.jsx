import React, {Component, PropTypes} from "react";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";
import RaisedButton from "material-ui/RaisedButton";
import TextField from "material-ui/TextField";
import FontIcon from "material-ui/FontIcon";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import * as Styles from "material-ui/styles";
import IconButton from "material-ui/IconButton";
import ContainerModalDialog from "./ContainerModalDialog";
import * as Actions from "../../actions/ContainerAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as theme from "../../RagnalogTheme";

const style = {
  margin: 12
};
const iconStyles = {
  marginRight: 24
};
class Containers extends Component {

  static propTypes = {
    isFetching: PropTypes.bool.isRequired,
    error: PropTypes.bool.isRequired,
    errorMessage: PropTypes.string,
    containers: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.string.isRequired,
        name: PropTypes.string.isRequired,
        description: PropTypes.string,
        state: PropTypes.string.isRequired
      })
    )
  };

  constructor(props) {
    super(props);
    this.state = {openDialog: false};
  }

  handleOpen = () => {
    this.setState({openDialog: true});
  };

  handleClose = () => {
    this.setState({openDialog: false});
  };

  componentWillMount() {
    this.props.fetchContainers();
  }

  render() {

    const rightIconMenu = (
      <IconMenu iconButtonElement={
          <IconButton>
            <MoreVertIcon color={theme.palette.accent1Color} />
          </IconButton>
        }
      >
        <MenuItem primaryText="View"/>
        <MenuItem primaryText="Edit"/>
        <MenuItem primaryText="Delete"/>
      </IconMenu>
    );

    return <div>
      <h2>Containers</h2>
      <TextField hintText="Search"/>
      <RaisedButton
        label="Add Container" style={style}
        icon={<FontIcon className="material-icons">add_circle</FontIcon>}
        onTouchTap={this.handleOpen}
      />
      <ContainerModalDialog
        open={this.state.openDialog}
      />
      <div>
        containers: {this.props.containers}
      </div>
      <Table selectable={false}>
        <TableHeader displaySelectAll={false}>
          <TableRow>
            <TableHeaderColumn>ID</TableHeaderColumn>
            <TableHeaderColumn>Name</TableHeaderColumn>
            <TableHeaderColumn>Status</TableHeaderColumn>
            <TableHeaderColumn></TableHeaderColumn>
          </TableRow>
        </TableHeader>
        <TableBody displayRowCheckbox={false} showRowHover={true}>
          <TableRow>
            <TableRowColumn>1</TableRowColumn>
            <TableRowColumn>John Smith</TableRowColumn>
            <TableRowColumn>Employed</TableRowColumn>
            <TableRowColumn>{rightIconMenu}</TableRowColumn>
          </TableRow>
          <TableRow>
            <TableRowColumn>2</TableRowColumn>
            <TableRowColumn>Randal White</TableRowColumn>
            <TableRowColumn>Unemployed</TableRowColumn>
          </TableRow>
          <TableRow>
            <TableRowColumn>3</TableRowColumn>
            <TableRowColumn>Stephanie Sanders</TableRowColumn>
            <TableRowColumn>Employed</TableRowColumn>
          </TableRow>
          <TableRow>
            <TableRowColumn>4</TableRowColumn>
            <TableRowColumn>Steve Brown</TableRowColumn>
            <TableRowColumn>Employed</TableRowColumn>
          </TableRow>
        </TableBody>
      </Table>
    </div>
  }
}

function mapStateToProps(state) {
  console.log(state.ContainerReducer);
  return {
    isFetching: state.ContainerReducer.isFetching,
    error: state.ContainerReducer.error,
    errorMessage: state.ContainerReducer.errorMessage,
    containers: state.ContainerReducer.containers
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(Actions, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(Containers);
